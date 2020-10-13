package com.example.noeTaptNoeFunnetAPP.post_item

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.noeTaptNoeFunnetAPP.R
import kotlinx.android.synthetic.main.fragment_date.view.*
import kotlinx.android.synthetic.main.fragment_form.view.*


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

            appNavigator.navigateFromDateToForm()
        }
        return  view
    }

    override fun onAttach(context: Context) { //få context til å senere kunne sende til deskription
        super.onAttach(context)
        appNavigator = context as AppNavigator
    }

}