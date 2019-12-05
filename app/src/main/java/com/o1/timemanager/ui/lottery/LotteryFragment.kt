package com.o1.timemanager.ui.lottery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.o1.timemanager.R
import kotlinx.android.synthetic.main.lottery.*

class LotteryFragment : Fragment() {
    private var lotteryViewModel: LotteryViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        lotteryViewModel =
            ViewModelProviders.of(this).get(LotteryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_knapsack, container, false)
//        val textView = root.findViewById<TextView>(R.id.text_slideshow)
//        lotteryViewModel!!.text
//            .observe(this, Observer { s -> textView.text = s })

        val lottery: ImageView = root.findViewById(R.id.lottery)
        lottery.setOnClickListener {
            lottery.setBackgroundResource(R.drawable.background_lotteried)
            lottery.setImageDrawable(null)
        }
        return root
    }
}