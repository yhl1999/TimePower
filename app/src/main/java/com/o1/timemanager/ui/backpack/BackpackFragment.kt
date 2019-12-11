package com.o1.timemanager.ui.backpack

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
import com.o1.timemanager.PlaidsContainer
import com.o1.timemanager.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BackpackFragment : Fragment() {
    lateinit var mainActivity: MainActivity
    lateinit var plaids: PlaidsContainer
    private var galleryViewModel: BackpackViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProviders.of(this).get(BackpackViewModel::class.java)
        val root =
            inflater.inflate(R.layout.fragment_information, container, false)
//        val textView = root.findViewById<TextView>(R.id.text_gallery)
//        galleryViewModel!!.text
//            .observe(this, Observer { s -> textView.text = s })

        plaids = root.findViewById(R.id.plaids)
        val icItem: ImageView = root.findViewById(R.id.ic_item)
        mainActivity = activity as MainActivity

        icItem.setImageResource(resources.getIdentifier(
            "ic_item_${mainActivity.user.get("role").asInt}_1",
            "drawable",
            context?.packageName
        ))
        plaids.setOnItemClickListener {
            icItem.setImageResource(
                resources.getIdentifier(
                    "ic_item_${it}_1",
                    "drawable",
                    context?.packageName
                )
            )
            mainActivity.api.post(JsonObject().apply {
                addProperty("apicode", 6)
                addProperty("userAcnt", mainActivity.userAcnt)
                addProperty("rid", it)
            }).enqueue(object : Callback<JsonObject>{
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    t.printStackTrace()
                    Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    response.body()?.let { res ->
                        if (res.get("statu").asBoolean) {
                            mainActivity.user.addProperty("role", it)
                        }
                    }
                }
            })
        }

        if (mainActivity.isLogin) {
            loadData()
        }

        return root
    }

    fun loadData() {
        mainActivity.api.post(JsonObject().apply {
            addProperty("apicode", 9)
            addProperty("userAcnt", mainActivity.userAcnt)
        }).enqueue(object : Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                response.body()?.let {
                    plaids.items.clear()
                    it.get("roleList").asJsonArray.forEach { item ->
                        plaids.items.add(item.asInt)
                    }
                    plaids.postInvalidate()
                }
            }
        })
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            loadData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        println("BackpackDestroy")
    }
}