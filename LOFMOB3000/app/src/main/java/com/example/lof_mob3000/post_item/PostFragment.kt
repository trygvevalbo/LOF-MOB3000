package com.example.lof_mob3000.post_item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.lof_mob3000.R


class PostFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.post_found_item, container, false)
        linearLayout = rootView.findViewById<View>(R.id.constraintLayout) as LinearLayout
        return rootView

    }
}