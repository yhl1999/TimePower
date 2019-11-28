package com.o1.timemanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var countdownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        change.setOnClickListener {
            if (circle.timerStarted) {
                change.text = "开始"
                circle.timerStarted = false
                countdownTimer.cancel()
            }
            else {
                countdownTimer = object : CountDownTimer(circle.minutes.toLong()*60*1000, 1000) {
                    override fun onFinish() {
                        println("Finish")
                    }

                    override fun onTick(millisUntilFinished: Long) {
                        circle.countDown()
                    }
                }
                change.text = "暂停"
                circle.timerStarted = true
                circle.filledValue = circle.minutes.toFloat()
                countdownTimer.start()
            }
        }

        team.setOnClickListener {

        }
    }
}
