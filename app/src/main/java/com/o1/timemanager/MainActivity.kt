package com.o1.timemanager

import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.lottery.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.backpack)

//        lottery.setOnClickListener {
//            lottery.setBackgroundResource(R.drawable.background_lotteried)
//            lottery.setImageDrawable(null)
//        }

//        change.setOnClickListener {
//            if (circle.timerStarted) {
//                change.text = "开始"
//                circle.stopTimer()
//            } else {
//                change.text = "暂停"
//                circle.startTimer()
//            }
//        }
//
//        var resNum = 1
//        val handler = Handler()
//        val runnable = object : Runnable {
//            override fun run() {
//                resNum = resNum % 3 + 1
//                ic_item.setImageResource(
//                    resources.getIdentifier(
//                        "ic_item_2_$resNum",
//                        "drawable",
//                        packageName
//                    )
//                )
//                handler.postDelayed(this, 500)
//            }
//        }
//
//        handler.postDelayed(runnable, 500)
//        team.setOnClickListener {
//            ic_item.clipBounds = Rect(0,0,104.px,112.px)
//        }
    }
    val Int.dp: Int
        get() = (this / Resources.getSystem().displayMetrics.density).toInt()
    val Int.px: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()
}
