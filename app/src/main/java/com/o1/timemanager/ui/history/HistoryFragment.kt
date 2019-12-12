package com.o1.timemanager.ui.history

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.vipulasri.timelineview.TimelineView
import com.google.gson.JsonObject
import com.o1.timemanager.*
import com.o1.timemanager.model.OrderStatus
import com.o1.timemanager.model.Orientation
import com.o1.timemanager.model.TimeLineModel
import com.o1.timemanager.model.TimelineAttributes
import kotlinx.android.synthetic.main.activity_history.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList

class HistoryFragment : Fragment() {

    private val mDataList = ArrayList<TimeLineModel>()
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mAttributes: TimelineAttributes

    lateinit var mainActivity: MainActivity
    lateinit var listHistory: ListView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.activity_history, container, false)
        mainActivity = activity as MainActivity

        mAttributes = TimelineAttributes(
            markerSize = (20f).px,
            markerColor = Color.LTGRAY,
            markerInCenter = true,
            markerLeftPadding = (0f).px,
            markerTopPadding = (0f).px,
            markerRightPadding = (0f).px,
            markerBottomPadding = (0f).px,
            linePadding = (2f).px,
            startLineColor = R.color.colorAccent,
            endLineColor = R.color.colorAccent,
            lineStyle = TimelineView.LineStyle.NORMAL,
            lineWidth = (2f).px,
            lineDashWidth = (4f).px,
            lineDashGap = (2f).px
        )

        loadData()
        return root
    }

    private fun initRecyclerView() {
        initAdapter()
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @SuppressLint("LongLogTag")
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun initAdapter() {
        mLayoutManager = if (mAttributes.orientation == Orientation.HORIZONTAL) {
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        } else {
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }

        recyclerView.apply {
            layoutManager = mLayoutManager
            adapter = TimeLineAdapter(mDataList, mAttributes)
        }
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
                mDataList.clear()
                for (data in jsondata) {
                    val jsonObject = data as JsonObject
                    println(jsonObject.get("startT").asString)
                    mDataList.add(
                        TimeLineModel(
                            jsonObject.get("actInfo").toString(),
                            jsonObject.get("startT").asString,
                            OrderStatus.COMPLETED
                        )
                    )
                }
                initRecyclerView()
            }

            override fun onFailure(
                call: Call<JsonObject?>,
                t: Throwable
            ) {
                Toast.makeText(context, "刷新失败！", Toast.LENGTH_SHORT).show()
            }
        })

    }
    val Float.dp: Float
        get() = this / Resources.getSystem().displayMetrics.density
    val Float.px: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()
}