package com.example.noeTaptNoeFunnetAPP

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.noeTaptNoeFunnetAPP.post_item.PostFoundItem
import com.example.noeTaptNoeFunnetAPP.post_item.PostLostItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_item_view.*
import kotlinx.android.synthetic.main.fragment_form.*
import kotlinx.android.synthetic.main.liste.*
import kotlinx.android.synthetic.main.liste.imageIC
import kotlinx.android.synthetic.main.liste.textIC_Besk
import kotlinx.android.synthetic.main.liste.textIC_Farge
import kotlinx.android.synthetic.main.liste.textIC_Navn
import kotlinx.android.synthetic.main.liste.textIC_Type
import kotlinx.android.synthetic.main.liste.textIC_dato

class ItemViewActivity : AppCompatActivity() , OnMapReadyCallback {
    private var email : String? = null
    private var itemLocation :  LatLng? = null
    private val database : FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_view)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //val actionBar : ActionBar? = supportActionBar
        //actionBar!!.setDisplayHomeAsUpEnabled(true)
       // actionBar.setDisplayShowHomeEnabled(true)
        var intent  = intent
        val aDocumentId = intent.getStringExtra("iDocumentId")
        val aName   = intent.getStringExtra("iName")
        val aType   = intent.getStringExtra("iType")
        val aTime   = intent.getStringExtra("iTime")
        val aColor  = intent.getStringExtra("iColor")
        val aDesk   = intent.getStringExtra("iDesk")
        val aImage  = intent.getStringExtra("iImage")
        val aContact = intent.getStringExtra("iContact")
        val aLat  = intent.getStringExtra("iLat")
        val aLng  = intent.getStringExtra("iLng")
        val aEmail = intent.getStringExtra("iEmail")


        supportActionBar?.title = aName
        textIC_Navn.text  = aName
        textIC_Type.text  = aType
        textIC_dato.text  = aTime
        textIC_Farge.text = aColor
        textIC_Besk.text  = aDesk
        textIC_Cont.text =  aContact
        Glide.with(this).load(aImage).into(imageIC)

//hent brukernavn av bruker som er logget inn
        val user = Firebase.auth.currentUser
        user?.let {
            for (profile in it.providerData) {
                email = profile.email
            }
        }

        //slettknapp & editknapp
        if(aEmail.toString() == email){
            delete_post_button.visibility = View.VISIBLE
            edit_button.visibility = View.VISIBLE

            delete_post_button.setOnClickListener {
                if (aDocumentId != null) {
                    deleteItem(aDocumentId)
                }
            }
            edit_button.setOnClickListener {
                if (aDocumentId != null){
                    if (aType != null) {
                        if(aType.contains("Funnet")) {

                            val intent1 = Intent(this, PostFoundItem::class.java)
                            intent1.putExtras(intent)
                            startActivity(intent1)
                        }else{
                            val intent1 = Intent(this, PostLostItem::class.java)
                            intent1.putExtras(intent)
                            startActivity(intent1)
                        }
                    }
                }
            }
        } else{
            delete_post_button.visibility = View.GONE;
        }

        //hent lokasjon og sett inn som LatLng
        if (aLat != null && aLng != null) {
            itemLocation = LatLng(aLat.toDouble(), aLng.toDouble())
        }
        //aktiver kart
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun deleteItem(aDocumentId : String){


        val builder = AlertDialog.Builder(this@ItemViewActivity)
        builder.setMessage("Er du sikker på at du vil slette denne posten?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                val docRef = database.collection("Posts").document(aDocumentId).delete()
                    .addOnSuccessListener { Toast.makeText(this, "Posten er slettet", Toast.LENGTH_LONG).show()
                        val intent1 = Intent(this, FrontPage::class.java)
                        startActivity(intent1)}
                    .addOnFailureListener { Toast.makeText(this, "klate ikke å slette post", Toast.LENGTH_LONG).show() }
            }
            .setNegativeButton("No") { dialog, id ->
                // Dismiss the dialog
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()


    }

    override fun onMapReady(googleMap: GoogleMap) {
        // Add a marker to item found or lost
        // and move the map's camera to the same location.

        googleMap.addMarker(
            itemLocation?.let {
                MarkerOptions()
                    .position(it)
                    .title("Marker of item")
            }
        )
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(itemLocation, 10f))

    }

}