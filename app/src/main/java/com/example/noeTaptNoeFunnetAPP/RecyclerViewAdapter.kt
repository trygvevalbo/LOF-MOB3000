package com.example.noeTaptNoeFunnetAPP

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.liste.view.*

class RecyclerViewAdapter(val arrayList: ArrayList<CardModel>, val context: Context) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {



    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(model: CardModel) {
            itemView.textIC_Navn.text = model.navn
            itemView.textIC_Type.text = model.type
            itemView.textIC_Farge.text = model.farge
            itemView.textIC_Besk.text = model.besk
            itemView.imageIC.setImageResource(model.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.liste, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])

        holder.itemView.setOnClickListener {
            val model = arrayList.get(position)
            var gNavn   : String = model.navn
            var gType   : String = model.type
            var gFarge  : String = model.farge
            var gBesk   : String = model.besk
            var gImage  : Int    = model.image

            val intent = Intent(context, ItemViewActivity::class.java)

            intent.putExtra("iNavn",  gNavn)
            intent.putExtra("iType",  gType)
            intent.putExtra("iFarge", gFarge)
            intent.putExtra("iBesk",  gBesk)
            intent.putExtra("iImage", gImage)

            context.startActivity(intent)
        }
    }
}