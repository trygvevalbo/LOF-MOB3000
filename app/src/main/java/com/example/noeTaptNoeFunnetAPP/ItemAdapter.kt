package com.example.noeTaptNoeFunnetAPP

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/*
    Kilder:
    Slett og endre funksjon  https://stackoverflow.com/questions/47986504/how-to-get-document-id-or-name-in-android-in-firestore-db-for-passing-on-to-anot
    Pass Data with intent https://www.youtube.com/watch?v=MJ-4QMUqLEc
 */

class ItemAdapter(options: FirestoreRecyclerOptions<Item>, val context: Context) :
    FirestoreRecyclerAdapter<Item, ItemAdapter.ItemAdapterVH>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapterVH {
        return ItemAdapterVH(
            LayoutInflater.from(parent.context).inflate(
                R.layout.liste,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemAdapterVH, position: Int, model: Item) {

        holder.ritemName.text = model.itemName
        holder.ritemColor.text = model.itemColor
        holder.ritemDesk.text = model.itemDesk
        holder.rpostTime.text = model.postTime
        holder.rpostType.text = model.postType
        Glide.with(holder.itemView.context).load(model.postImage).into(holder.rpostImage)


        holder.itemView.setOnClickListener {
            val user = Firebase.auth.currentUser

            if (user != null) {
                var cName   : String? = model.itemName
                var cColor  : String? = model.itemColor
                var cContact : String? = model.postContact
                var cDesk  : String? = model.itemDesk
                var cTime  : String? = model.postTime
                var cType  : String? = model.postType
                var cImage : String? =  model.postImage
                var cLat : String? =  model.itemLat
                var cLng : String? =  model.itemLng
                var cEmail : String? = model.userEmail
                var cId : String? = model.postId


            val intent = Intent(context, ItemViewActivity::class.java)
            val snapshot = snapshots.getSnapshot(holder.adapterPosition)
             var cDocumentId = snapshot.id
            intent.putExtra("iDocumentId", cDocumentId )
            intent.putExtra("iName", cName)
            intent.putExtra("iType", cType)
            intent.putExtra("iTime", cTime)
            intent.putExtra("iColor", cColor)
            intent.putExtra("iDesk", cDesk)
            intent.putExtra("iImage", cImage)
                intent.putExtra("iContact", cContact)
            intent.putExtra("iLat", cLat)
            intent.putExtra("iLng", cLng)
            intent.putExtra("iEmail", cEmail)
            intent.putExtra("iId", cId)


            context.startActivity(intent)


     
            } else {
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
            }


        }

    }
    class ItemAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var ritemColor: TextView =itemView.findViewById(R.id.textIC_Farge)
        var ritemDesk: TextView =itemView.findViewById(R.id.textIC_Besk)
        var ritemName: TextView =itemView.findViewById(R.id.textIC_Navn)
        var rpostImage: ImageView = itemView.findViewById(R.id.imageIC)
        var rpostTime: TextView =itemView.findViewById(R.id.textIC_dato)
        var rpostType: TextView =itemView.findViewById(R.id.textIC_Type)
    }
}

