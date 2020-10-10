package com.example.noeTaptNoeFunnetAPP.post_item

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.noeTaptNoeFunnetAPP.R
import kotlinx.android.synthetic.main.fragment_description.view.*


class DescriptionFragment : Fragment() {

private var description : String= ""
    private lateinit var appNavigator: AppNavigator



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {





        val view =inflater.inflate(R.layout.fragment_description, container, false)


        view.done_button.setOnClickListener {
        val description= view?.findViewById(R.id.description) as? EditText
            appNavigator.navigateToForm()
        }

        return view
    }


   override fun  onStop() {
       super.onStop()
       val description= view?.findViewById(R.id.description) as? EditText
       val descriptionString = description?.text.toString()
       if (descriptionString != null) {

           passData(descriptionString)
       }
    }

    lateinit var dataPasser: AppNavigator

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as AppNavigator
        appNavigator = context as AppNavigator
    }

    fun passData(data: String){
        dataPasser.onDescriptionPass(data)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }






}