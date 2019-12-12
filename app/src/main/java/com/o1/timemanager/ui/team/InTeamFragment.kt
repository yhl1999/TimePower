package com.o1.timemanager.ui.team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.o1.timemanager.MainActivity
import com.o1.timemanager.R
import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.Delivery
import java.lang.Exception

class InTeamFragment : Fragment() {
    lateinit var mainActivity: MainActivity
    lateinit var teamsLayout: LinearLayout
    var members: MutableSet<String> = mutableSetOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        val root = inflater.inflate(R.layout.in_team, container, false)
        teamsLayout = root.findViewById(R.id.teams)
        val teamCancel: Button = root.findViewById(R.id.team_cancel)
        val teamStart: Button = root.findViewById(R.id.team_start)
        val uuid: TextView = root.findViewById(R.id.uuid)
        val qrCode: ImageView = root.findViewById(R.id.qr_code)

        uuid.text = mainActivity.teamUUID
        mainActivity.runOnUiThread {
            val member = inflater.inflate(R.layout.member, teamsLayout, false)
            val userAcnt: TextView = member.findViewById(R.id.userAcnt)
            userAcnt.text = mainActivity.userAcnt
            teamsLayout.addView(member)
        }
        members.add(mainActivity.userAcnt)

        if (mainActivity.teamUUID != "") {
            Thread {
                mainActivity.conn = ConnectionFactory().apply {
                    host = "121.36.56.36"
                    username = "timepower"
                    password = "timepower"
                }.newConnection()
                mainActivity.channel = mainActivity.conn.createChannel()
                println("channel created")

                println("publish")
                mainActivity.exchangeName = "team_${mainActivity.teamUUID}"
                mainActivity.channel.exchangeDeclare(
                    mainActivity.exchangeName,
                    BuiltinExchangeType.FANOUT
                )
                val queueName = mainActivity.channel.queueDeclare().queue
                mainActivity.channel.queueBind(
                    queueName,
                    mainActivity.exchangeName,
                    ""
                )
                mainActivity.channel.basicPublish(
                    mainActivity.exchangeName,
                    "",
                    null,
                    "join_${mainActivity.userAcnt}".toByteArray()
                )
                mainActivity.channel.basicConsume(
                    queueName,
                    true,
                    { consumerTag: String, delivery: Delivery ->
                        var message = delivery.body.toString(Charsets.UTF_8)

                        if (message.startsWith("start_")) {
                            message = message.substring(6)
                            val time = message.split("_")
                            mainActivity.circle.minutes = time[0].toInt()
                            mainActivity.circle.seconds = time[1].toInt()
                            mainActivity.runOnUiThread {
                                mainActivity.teamBegin()
                            }
                        }
                        else if (mainActivity.isCaptain) {
                            if (message.startsWith("join_")) {
                                message = message.substring(5)

                                members.add(message)

                                refreshMembers(layoutInflater)

                                mainActivity.channel.basicPublish(
                                    mainActivity.exchangeName,
                                    "",
                                    null,
                                    "total_${members.joinToString(separator = "&")}".toByteArray()
                                )
                            }
                            else if (message.startsWith("leave_")) {
                                message = message.substring(6)
                                members.remove(message)

                                refreshMembers(layoutInflater)

                                mainActivity.channel.basicPublish(
                                    mainActivity.exchangeName,
                                    "",
                                    null,
                                    "total_${members.joinToString(separator = "&")}".toByteArray()
                                )
                            }
                        } else {
                            if (message.startsWith("total_")) {
                                message = message.substring(6)

                                members = message.split("&").toMutableSet()

                                println(members)
                                refreshMembers(inflater)
                            }
                            if (message == "close") {
                                mainActivity.leaveTeam()
                            }
                        }
                        println(message)
                    },
                    { _ -> })
            }.start()
        }

        teamStart.setOnClickListener {
            if (mainActivity.isCaptain) {
                println("start_${mainActivity.circle.minutes}_${mainActivity.circle.seconds}")
                Thread {
                    mainActivity.channel.basicPublish(
                        mainActivity.exchangeName,
                        "",
                        null,
                        "start_${mainActivity.circle.minutes}_${mainActivity.circle.seconds}".toByteArray()
                    )
                }.start()
            }
        }
        teamCancel.setOnClickListener {
            mainActivity.leaveTeam()
        }

        val qrCodeImage =
            BarcodeEncoder().encodeBitmap(mainActivity.teamUUID, BarcodeFormat.QR_CODE, 400, 400)
        qrCode.setImageBitmap(qrCodeImage)

        return root
    }

    private fun refreshMembers(inflater: LayoutInflater) {
        mainActivity.runOnUiThread {
            teamsLayout.removeAllViews()
            for (member in members) {
                val memberView: TextView =
                    inflater.inflate(R.layout.member, teamsLayout, false)
                        .findViewById(R.id.userAcnt)
                memberView.text = member
                teamsLayout.addView(memberView)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Thread {
            try {
                if (mainActivity.channel.isOpen) {
                    if (mainActivity.isCaptain) {
                        mainActivity.channel.basicPublish(mainActivity.exchangeName, "", null, "close".toByteArray())
                        mainActivity.channel.exchangeDelete(mainActivity.exchangeName)
                    }
                    else {
                        mainActivity.channel.basicPublish(mainActivity.exchangeName, "", null, "leave_${mainActivity.userAcnt}".toByteArray())
                    }
                    mainActivity.channel.close()
                }
                if (mainActivity.conn.isOpen) {
                    mainActivity.conn.close()
                }
            } catch (ignore: Exception) {}
        }.start()
    }
}