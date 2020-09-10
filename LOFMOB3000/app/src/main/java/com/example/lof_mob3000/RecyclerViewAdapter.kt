package com.example.lof_mob3000

import android.content.Context
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.liste.view.*

class RecyclerViewAdapter(val arrayList: ArrayList<CardModel>, val context: Context) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {



    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(model: CardModel) {
            itemView.textIC_Navn.text = model.navn
            itemView.textIC_Farge.text = model.farge
            itemView.textIC_Besk.text = model.besk
            itemView.imageIC.setImageResource(model.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.liste, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}