package com.o1.timemanager.ui.home

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.o1.timemanager.Circle
import com.o1.timemanager.R

class HomeFragment : Fragment() {
    private var homeViewModel: HomeViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
//        val textView = root.findViewById<TextView>(R.id.text_home)
//        homeViewModel!!.text.observe(this, Observer { s -> textView.text = s })
        val change: Button = root.findViewById(R.id.change)
        val circle: Circle = root.findViewById(R.id.circle)
        val icItem: ImageView = root.findViewById(R.id.ic_item)
        val team: Button = root.findViewById(R.id.team)
        change.setOnClickListener {
            if (circle.timerStarted) {
                change.text = "开始"
                circle.stopTimer()
            } else {
                change.text = "暂停"
                circle.startTimer()
            }
        }

        var resNum = 1
        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                resNum = resNum % 2 + 1
                if (isAdded) {
                    icItem.setImageResource(
                        resources.getIdentifier(
                            "ic_item_2_$resNum",
                            "drawable",
                            context?.packageName
                        )
                    )
                    handler.postDelayed(this, 500)
                }
            }
        }

        handler.postDelayed(runnable, 500)
        team.setOnClickListener {
        }
        return root
    }
}