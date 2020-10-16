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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.example.noeTaptNoeFunnetAPP.R
import kotlinx.android.synthetic.main.fragment_description.view.*


class DescriptionFragment : Fragment() {
    //private val args: FormFragmentArgs by navArgs()

    private lateinit var appNavigator: AppNavigator
    private var model: FormViewModel?=null




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_description, container, false)

        model= ViewModelProviders.of(requireActivity()).get(FormViewModel::class.java)
        val description= view?.findViewById(R.id.description) as? EditText

        model!!.savedDescription.observe(viewLifecycleOwner, object: Observer<Any> {
            override fun onChanged(o: Any?) {

                description?.setText(o!!.toString())
            }
        })



        view.done_button.setOnClickListener {


            model= ViewModelProviders.of(requireActivity()).get(FormViewModel::class.java)

            model!!.setDescription(description?.text.toString()) // set verdi

            appNavigator.navigateToForm()
        }

        return view
    }






    override fun onAttach(context: Context) {
        super.onAttach(context)
       appNavigator = context as AppNavigator
    }


}