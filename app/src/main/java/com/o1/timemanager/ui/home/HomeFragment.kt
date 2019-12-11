package com.o1.timemanager.ui.home

import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.o1.timemanager.Circle
import com.o1.timemanager.MainActivity
import com.o1.timemanager.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*

class HomeFragment : Fragment() {
    private var homeViewModel: HomeViewModel? = null
    lateinit var begin: Button
    lateinit var circle: Circle
    lateinit var icItem: ImageView
    lateinit var team: Button
    lateinit var coinValue: TextView
    lateinit var mainActivity: MainActivity
    var added = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
//        val textView = root.findViewById<TextView>(R.id.text_home)
//        homeViewModel!!.text.observe(this, Observer { s -> textView.text = s })
        val teamCheck: CheckBox = root.findViewById(R.id.isCoop)

        begin = root.findViewById(R.id.begin)
        circle = root.findViewById(R.id.circle)
        icItem = root.findViewById(R.id.ic_item)
        team = root.findViewById(R.id.team)
        coinValue = root.findViewById(R.id.coin_value)

        coinValue.typeface = Typeface.createFromAsset(context?.assets, "fonts/Lato-Light.ttf")
        begin.setOnClickListener {
            if (teamCheck.isChecked) {
                mainActivity.joinTeam(UUID.randomUUID().toString(), true)
            }
            else {
                if (circle.timerStarted) {
                    begin.text = "开始"
                    circle.stopTimer()
                } else {
                    begin.text = "暂停"
                    circle.startTimer()
                }
            }
        }
        team.setOnClickListener {
            teamCheck.toggle()
        }
        teamCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                begin.text = "开始组队"
            }
            else {
                begin.text = "开始"
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = activity as MainActivity
        mainActivity.circle = circle
        if (mainActivity.isLogin) {

            loadData()

        }
    }

    private fun loadData() {
        mainActivity.api.post(JsonObject().apply {
            addProperty("apicode", 10)
            addProperty("userAcnt", mainActivity.userAcnt)
        }).enqueue(object : Callback<JsonObject> {
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
                    if (!added) {
                        added = true

                        var resNum = 1
                        val handler = Handler()
                        val runnable = object : Runnable {
                            override fun run() {
                                val role = mainActivity.user.get("role").asInt
                                resNum = resNum % (if (role == 1) 3 else 2) + 1
                                try {
                                    icItem.setImageResource(
                                        resources.getIdentifier(
                                            "ic_item_${role}_$resNum",
                                            "drawable",
                                            context?.packageName
                                        )
                                    )
                                    handler.postDelayed(this, 500)
                                } catch (ignore: Exception) {}
                            }
                        }
                        handler.postDelayed(runnable, 500)
                    }
                }
            }
        })

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            loadData()
        }
    }
}