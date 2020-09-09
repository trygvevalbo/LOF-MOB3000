package com.example.lof_mob3000

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.*

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private val itemNavn = arrayOf("text1", "text2", "text3", "text4", "text5")

    private val itemFarge = arrayOf("Svart", "Hvit", "Rød", "Blå", "Grønn")

    private val itemImage = intArrayOf(
        R.drawable.thonk,
        R.drawable.bigusbrainus,
        R.drawable.throwup,
        R.drawable.cat,
        R.drawable.donkey
    )

    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){

        var image   : ImageView
        var textNavn   : TextView
        var textFarge    : TextView
        var textBesk   : TextView

        init {
            image       = itemView.findViewById(R.id.imageIC)
            textNavn    = itemView.findViewById(R.id.textIC_Navn)
            textFarge   = itemView.findViewById(R.id.textIC_Farge)
            textBesk    = itemView.findViewById(R.id.textIC_Beskrivelse)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.liste,parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textNavn.text = itemNavn [position]
        holder.textFarge.text = itemFarge [position]
        holder.image.setImageResource(itemImage [position])

        holder.itemView.setOnClickListener{v: View ->

            Toast.makeText(v.context, "Clicked on the item", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return itemNavn.size
    }

}