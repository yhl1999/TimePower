package com.o1.timemanager.ui.backpack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.o1.timemanager.PlaidsContainer
import com.o1.timemanager.R

class BackpackFragment : Fragment() {
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

        val plaids: PlaidsContainer = root.findViewById(R.id.plaids)
        val icItem: ImageView = root.findViewById(R.id.ic_item)
        plaids.setOnItemClickListener {
            icItem.setImageResource(
                resources.getIdentifier(
                    "ic_item_${it}_1",
                    "drawable",
                    context?.packageName
                )
            )
        }

        return root
    }
}