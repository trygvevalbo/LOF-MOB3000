package com.example.lof_mob3000.post_item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lof_mob3000.R
import kotlinx.android.synthetic.main.post_found_item.view.*

class PostFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
       //inflate layout
        val binding: PostFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.post_found_item, container, false)
        binding.post_button_found_item.setOnClickListener{
            findNavController().navigate(Title)
        }
    }
}