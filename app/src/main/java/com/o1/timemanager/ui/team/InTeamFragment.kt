package com.o1.timemanager.ui.team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.JsonObject
import com.o1.timemanager.MainActivity
import com.o1.timemanager.R
import java.util.*

class InTeamFragment : Fragment() {
    val timer = Timer()
    lateinit var timerTask: TimerTask
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        val root = inflater.inflate(R.layout.in_team, container, false)
        timerTask = object : TimerTask(){
            override fun run() {
                mainActivity.api.post(JsonObject().apply {

                })
            }
        }
        return root
    }
}