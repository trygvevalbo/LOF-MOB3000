package com.example.lof_mob3000.post_item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.lof_mob3000.R
import com.example.lof_mob3000.databinding.PostFoundItemBinding


class PostFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val binding: PostFoundItemBinding = DataBindingUtil.inflate(
            inflater, R.layout.post_found_item, container, false)


        return binding.root
    }
}