package com.example.noeTaptNoeFunnetAPP

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
<<<<<<< HEAD

import kotlinx.android.synthetic.main.activity_item_view.view.*

=======
>>>>>>> parent of 3e9582d... cardmodel gone
import kotlinx.android.synthetic.main.liste.view.*

class RecyclerViewAdapter(val arrayList: ArrayList<CardModel>, val context: Context) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {



    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {


<<<<<<< HEAD

        fun bindItems(model: Item) {
            itemView.textIC_Navn.text    = model.nameOfItem
            itemView.textIC_Farge.text   = model.colorOfFound
            itemView.textIC_Besk.text    = model.descriptionOfFound
            itemView.textIC_time.text    = model.time
            itemView.textIC_Type.text    = model.typeOfPost
            itemView.imageIC.setImageResource(R.drawable.bigusbrainus)

=======
        fun bindItems(model: CardModel) {
            itemView.textIC_Navn.text = model.navn
            itemView.textIC_Type.text = model.type
            itemView.textIC_dato.text = model.dato
            itemView.textIC_Farge.text = model.farge
            itemView.textIC_Besk.text = model.besk
            itemView.imageIC.setImageResource(model.image)
>>>>>>> parent of 3e9582d... cardmodel gone
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
            var gDato   : String = model.dato
            var gFarge  : String = model.farge
            var gBesk   : String = model.besk
            var gImage  : Int    = model.image

            val intent = Intent(context, ItemViewActivity::class.java)

            intent.putExtra("iNavn",  gNavn)
            intent.putExtra("iType",  gType)
            intent.putExtra("iDato",  gDato)
            intent.putExtra("iFarge", gFarge)
            intent.putExtra("iBesk",  gBesk)
            intent.putExtra("iImage", gImage)

            context.startActivity(intent)
        }
    }


}