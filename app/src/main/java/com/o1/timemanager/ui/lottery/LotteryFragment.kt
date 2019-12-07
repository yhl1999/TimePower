package com.o1.timemanager.ui.lottery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.gson.JsonObject
import com.o1.timemanager.MainActivity
import com.o1.timemanager.R
import kotlinx.android.synthetic.main.lottery.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LotteryFragment : Fragment() {
    private var lotteryViewModel: LotteryViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        lotteryViewModel =
            ViewModelProviders.of(this).get(LotteryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_knapsack, container, false)
//        val textView = root.findViewById<TextView>(R.id.text_slideshow)
//        lotteryViewModel!!.text
//            .observe(this, Observer { s -> textView.text = s })

        val mainActivity: MainActivity = activity as MainActivity
        val lottery: ImageView = root.findViewById(R.id.lottery)
        lottery.setOnClickListener {
            mainActivity.api.post(JsonObject().apply {
                addProperty("apicode", 13)
                addProperty("userAcnt", mainActivity.userAcnt)
            }).enqueue(object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    t.printStackTrace()
                    Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.body()?.get("statu")?.asInt == 0){
                        Toast.makeText(context, "金币不足", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        lottery.setBackgroundResource(R.drawable.background_lotteried)
                        lottery.setImageDrawable(
                            context?.resources?.getDrawable(
                                resources.getIdentifier(
                                    "ic_item_${response.body()?.get("statu")?.asInt}_1",
                                    "drawable",
                                    context?.packageName
                                ),
                                null
                            )
                        )
                    }
                }
            })
        }
        return root
    }
}