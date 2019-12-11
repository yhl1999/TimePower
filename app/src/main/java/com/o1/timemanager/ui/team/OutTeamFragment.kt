package com.o1.timemanager.ui.team

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.JsonObject
import com.google.zxing.integration.android.IntentIntegrator
import com.o1.timemanager.MainActivity
import com.o1.timemanager.R
import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.ConnectionFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

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
        val scan: Button = root.findViewById(R.id.scan)
        addTeam.setOnClickListener {
            mainActivity.joinTeam(teamId.text.toString())
        }
        scan.setOnClickListener {
            IntentIntegrator(mainActivity)
                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                .setCameraId(0)
                .setBeepEnabled(true)
                .initiateScan()
        }
        return root
    }

}