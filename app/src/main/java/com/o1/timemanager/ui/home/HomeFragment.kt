package com.o1.timemanager.ui.home

import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.o1.timemanager.Circle
import com.o1.timemanager.MainActivity
import com.o1.timemanager.R
import com.o1.timemanager.ui.team.InTeamFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private var homeViewModel: HomeViewModel? = null
    lateinit var change: Button
    lateinit var circle: Circle
    lateinit var icItem: ImageView
    lateinit var team: Button
    lateinit var coinValue: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
//        val textView = root.findViewById<TextView>(R.id.text_home)
//        homeViewModel!!.text.observe(this, Observer { s -> textView.text = s })
        change = root.findViewById(R.id.change)
        circle = root.findViewById(R.id.circle)
        icItem = root.findViewById(R.id.ic_item)
        team = root.findViewById(R.id.team)
        coinValue = root.findViewById(R.id.coin_value)

        coinValue.typeface = Typeface.createFromAsset(context?.assets, "fonts/Lato-Light.ttf")
        change.setOnClickListener {
            if (circle.timerStarted) {
                change.text = "开始"
                circle.stopTimer()
            } else {
                change.text = "暂停"
                circle.startTimer()
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainActivity = activity as MainActivity
        val userAcnt = mainActivity.userAcnt
        val api = mainActivity.api
        mainActivity.circle = circle
        if (mainActivity.isLogin) {

            val body = JsonObject().apply {
                addProperty("apicode", 10)
                addProperty("userAcnt", userAcnt)
            }

            api.post(body).enqueue(object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    t.printStackTrace()
                    Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    println(response.body())
                    response.body()?.let {
                        mainActivity.user = it
                        if (mainActivity.user.get("headpic") != null) {
                            /* TODO 设置头像 */
                        }
                        coinValue.text = mainActivity.user.get("coin").asInt.toString()
                        var resNum = 1
                        val handler = Handler()
                        val runnable = object : Runnable {
                            override fun run() {
                                val role = mainActivity.user.get("role").asInt
                                resNum = resNum % (if (role == 1) 3 else 2) + 1
                                if (isAdded) {
                                    icItem.setImageResource(
                                        resources.getIdentifier(
                                            "ic_item_${role}_$resNum",
                                            "drawable",
                                            context?.packageName
                                        )
                                    )
                                    handler.postDelayed(this, 500)
                                }
                            }
                        }

                        handler.postDelayed(runnable, 500)
                    }
                }
            })


            team.setOnClickListener {
                mainActivity.api.post(JsonObject().apply {
                    addProperty("apicode", 14)
                    add("userAcnt", JsonArray().apply {
                        add(mainActivity.userAcnt)
                    })
                    addProperty("actType", 0)
                    add("actInfo", JsonObject())
                }).enqueue(object : Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<JsonObject>,
                        response: Response<JsonObject>
                    ) {
                        Toast.makeText(
                            context,
                            "队伍编号：${response.body()?.get("statu")?.asInt.toString()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            }
        }
    }
}