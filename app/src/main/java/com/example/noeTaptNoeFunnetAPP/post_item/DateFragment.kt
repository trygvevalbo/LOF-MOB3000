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
import androidx.fragment.app.Fragment
import com.example.noeTaptNoeFunnetAPP.R
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
        val  view = inflater.inflate(R.layout.fragment_date, container, false)
        view.selected_date_button.setOnClickListener {
            val datePicker: DatePicker= view.findViewById(R.id.DatePicker) as DatePicker
            val getDate  = Calendar.getInstance()
            datePicker.init(getDate.get(Calendar.DAY_OF_MONTH),getDate.get(Calendar.MONDAY),getDate.get(Calendar.YEAR))

            { view, year, month, day ->
                val month = month + 1
                val msg = "You selected: $day/$month/$year"
                Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
            }


            appNavigator.navigateFromDateToForm()
        }
        return  view
    }

    override fun onAttach(context: Context) { //få context til å senere kunne sende til description
        super.onAttach(context)
        appNavigator = context as AppNavigator
    }

}