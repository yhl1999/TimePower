package com.o1.timemanager.ui.team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.JsonObject
import com.o1.timemanager.MainActivity
import com.o1.timemanager.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OutTeamFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.out_team, container, false)
        val mainActivity = activity as MainActivity

        val addTeam: Button = root.findViewById(R.id.add_team)
        val teamId: TextView = root.findViewById(R.id.team_id)
        addTeam.setOnClickListener {
            mainActivity.teamId = teamId.text.toString().toInt()
            mainActivity.api.post(JsonObject().apply {
                addProperty("apicode", 15)
                addProperty("userAcnt", mainActivity.userAcnt)
                addProperty("teamIndex", mainActivity.teamId)
            }).enqueue(object : Callback<JsonObject>{
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    mainActivity.transaction.replace(R.id.nav_host_fragment, mainActivity.homeFragment).commit()
                    mainActivity.atHome = true
                    mainActivity.circle.startTimer()
                }
            })
        }
        return root
    }
}