package com.example.noeTaptNoeFunnetAPP.post_item

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.noeTaptNoeFunnetAPP.R
import com.example.noeTaptNoeFunnetAPP.databinding.FragmentDateBinding
import com.example.noeTaptNoeFunnetAPP.databinding.FragmentMapsFullScreenBinding
import com.google.android.material.datepicker.MaterialDatePicker.Builder.datePicker
import kotlinx.android.synthetic.main.fragment_date.view.*
import java.util.*


class dateFragment : Fragment() {

    private lateinit var appNavigator: AppNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentDateBinding>(inflater, R.layout.fragment_date,
            container, false)
        binding.selectedDateButton.setOnClickListener {

        Toast.makeText(requireContext(), "hei", Toast.LENGTH_SHORT).show()

            appNavigator.navigateFromDateToForm()
        }
        return  binding.root
    }

    override fun onAttach(context: Context) { //få context til å senere kunne sende til description
        super.onAttach(context)
        appNavigator = context as AppNavigator
    }

}