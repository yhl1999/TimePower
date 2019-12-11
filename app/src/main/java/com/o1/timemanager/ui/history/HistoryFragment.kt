package com.o1.timemanager.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.JsonObject
import com.o1.timemanager.Api
import com.o1.timemanager.MainActivity
import com.o1.timemanager.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HistoryFragment : Fragment() {
    lateinit var mainActivity: MainActivity
    lateinit var listHistory: ListView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.activity_history, container, false)
        mainActivity = activity as MainActivity

        listHistory = root.findViewById(R.id.listViewHistory)
        loadData()

        return root
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            loadData()
        }
    }

    fun loadData() {

        val retrofit = Retrofit.Builder()
            .baseUrl("http://121.36.56.36:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(Api::class.java)
        val body = JsonObject()
        body.addProperty("apicode", 12)
        body.addProperty("userAcnt", mainActivity.userAcnt)
        api.post(body).enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>
            ) {
                if (response.body() == null) {
                    return
                }
                //解析返回的JSONs
                val jsondata = response.body()!!["actList"].asJsonArray
                val dataList: MutableList<String> = mutableListOf()
                var i = 0
                val size = jsondata.size()
                while (i < size) {
                    dataList.add(jsondata[i].toString())
                    i++
                }
                //适配器
                val adapter = ArrayAdapter(
                    context!!,
                    android.R.layout.simple_list_item_1,
                    dataList
                )
                listHistory.adapter = adapter
            }

            override fun onFailure(
                call: Call<JsonObject?>,
                t: Throwable
            ) {
                Toast.makeText(context, "刷新失败！", Toast.LENGTH_SHORT).show()
            }
        })

    }
}