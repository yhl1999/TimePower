package com.o1.timemanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        change.setOnClickListener {
            circle.filledValue = (circle.filledValue)%110f+10f
            circle.postInvalidate()
        }
    }
}
