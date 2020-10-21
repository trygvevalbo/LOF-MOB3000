package com.example.noeTaptNoeFunnetAPP

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_item_view.view.*
import kotlinx.android.synthetic.main.liste.view.*
import kotlinx.android.synthetic.main.liste.view.imageIC
import kotlinx.android.synthetic.main.liste.view.textIC_Besk
import kotlinx.android.synthetic.main.liste.view.textIC_Farge
import kotlinx.android.synthetic.main.liste.view.textIC_Navn

class RecyclerViewAdapter(val arrayList: ArrayList<Item>, val context: Context) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {



    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bindItems(model: Item) {
            itemView.textIC_Navn.text    = model.nameOfItem
            itemView.textIC_Farge.text   = model.colorOfFound
            itemView.textIC_Besk.text    = model.descriptionOfFound
            itemView.textIC_time.text    = model.time
            itemView.textIC_Type.text    = model.typeOfPost
            itemView.imageIC.setImageResource(R.drawable.bigusbrainus)
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
            var gNavn   : String = model.nameOfItem
            var gFarge  : String = model.colorOfFound
            var gBesk   : String = model.descriptionOfFound
            var gTime   : String = model.time
            var gType   : String = model.typeOfPost
            var gImage  : String = model.postImage




            val intent = Intent(context, ItemViewActivity::class.java)

            intent.putExtra("iNavn",  gNavn)
            intent.putExtra("iFarge", gFarge)
            intent.putExtra("iBesk",  gBesk)
            intent.putExtra("iTime",  gTime)
            intent.putExtra("iType",  gType)
            intent.putExtra("iImage", gImage)

            context.startActivity(intent)
        }
    }


}