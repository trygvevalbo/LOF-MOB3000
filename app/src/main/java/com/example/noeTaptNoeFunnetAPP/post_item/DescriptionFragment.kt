package com.example.noeTaptNoeFunnetAPP.post_item

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.navArgs
import com.example.noeTaptNoeFunnetAPP.R
import kotlinx.android.synthetic.main.fragment_description.view.*


class DescriptionFragment : Fragment() {
    private val args: FormFragmentArgs by navArgs()
private var description : String= ""
    private lateinit var appNavigator: AppNavigator

    lateinit var dataPasser: AppNavigator


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_description, container, false)





        val description= args.description
        if (description != "") {
            val textEdit: EditText = view.findViewById(R.id.description) as EditText
            if (textEdit != null) {
                textEdit.setText(description, TextView.BufferType.EDITABLE)
            }
        }

        view.done_button.setOnClickListener {
            val description= view?.findViewById(R.id.description) as? EditText
            val descriptionString = description?.text.toString()
            setFragmentResult("DESCRIPTION_KEY", bundleOf("description" to descriptionString))

            passData(descriptionString)
            appNavigator.navigateToForm()
        }

        return view
    }






    override fun onAttach(context: Context) {
        super.onAttach(context)
       appNavigator = context as AppNavigator
        dataPasser = context
    }

    fun passData(data: String){
        dataPasser.onDataPass(data)
    }


}