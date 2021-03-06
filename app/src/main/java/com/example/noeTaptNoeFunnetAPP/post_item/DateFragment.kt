package com.example.noeTaptNoeFunnetAPP.post_item

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.noeTaptNoeFunnetAPP.R
import com.example.noeTaptNoeFunnetAPP.databinding.FragmentDateBinding


class dateFragment : Fragment() {

    private lateinit var appNavigator: AppNavigator
    private var model: FormViewModel?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentDateBinding>(
            inflater, R.layout.fragment_date,
            container, false
        )


        binding.doneDateButton.setOnClickListener {
            model= ViewModelProviders.of(requireActivity()).get(FormViewModel::class.java)
            binding.viewModel = model//attach your viewModel to xml
            binding.viewModel?.savedTime?.value = binding.datePicker.dayOfMonth.toString() +"/"+(binding.datePicker.month+1).toString() +"/"+ binding.datePicker.year.toString()
            appNavigator.navigateFromDateToForm()
        }

            val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            model= ViewModelProviders.of(requireActivity()).get(FormViewModel::class.java)
            binding.viewModel = model//attach your viewModel to xml
            binding.viewModel?.savedTime?.value = binding.datePicker.dayOfMonth.toString() +"/"+(binding.datePicker.month+1).toString() +"/"+ binding.datePicker.year.toString()
            appNavigator.navigateFromDateToForm()
        }
        return  binding.root
    }



    override fun onAttach(context: Context) { //få context til å senere kunne sende til description
        super.onAttach(context)
        appNavigator = context as AppNavigator
    }

}