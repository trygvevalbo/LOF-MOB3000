package com.example.noeTaptNoeFunnetAPP.post_item

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.noeTaptNoeFunnetAPP.R
import kotlinx.android.synthetic.main.fragment_description.view.*


class DescriptionFragment : Fragment() {
    private lateinit var appNavigator: AppNavigator
    private var model: FormViewModel?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_description, container, false)

        model= ViewModelProviders.of(requireActivity()).get(FormViewModel::class.java)
        val description= view?.findViewById(R.id.description) as? EditText

        model!!.savedDescription.observe(viewLifecycleOwner,
            { o -> description?.setText(o!!.toString()) })

        val doneButton = view?.findViewById(R.id.done_description_button) as? Button
        doneButton?.setOnClickListener {
            model= ViewModelProviders.of(requireActivity()).get(FormViewModel::class.java)

            model!!.setDescription(description?.text.toString()) // set verdi

            appNavigator.navigateToForm()
        }
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
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