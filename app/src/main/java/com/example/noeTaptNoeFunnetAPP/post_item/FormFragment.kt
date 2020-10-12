package com.example.noeTaptNoeFunnetAPP.post_item

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.noeTaptNoeFunnetAPP.FrontPage
import com.example.noeTaptNoeFunnetAPP.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_form.*
import kotlinx.android.synthetic.main.fragment_form.view.*


class FormFragment : Fragment() {
    private val args: FormFragmentArgs by navArgs()

    private var mUri: Uri? = null //deklarasjon av url til bilde

    private lateinit var appNavigator: AppNavigator //interface til å sende til description fragment



    lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap

    private val IMAGE_CAPTURE_CODE = 1001 // camera funksjon https://www.youtube.com/watch?v=3gkAoF90RZ4
    private val PERMISSION_CODE = 1000
    private val RequestCode = 438
    var image_uri: Uri? = null
    private var storageRef: StorageReference? = null
    private var coverChecker: String? = ""
    lateinit var dataPasser: AppNavigator

    override fun onAttach(context: Context) { //få context til å senere kunne sende til deskription
        super.onAttach(context)
        appNavigator = context as AppNavigator
        dataPasser = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_form, container, false)


        if (savedInstanceState != null) {
            mUri = savedInstanceState.getParcelable("uri")
            image_view.setImageURI(mUri)
        }

        mapManager()


        clickManager(view)


        if (args.description != null) {
            // args.description.take(10)

            val textView: TextView = view.findViewById(R.id.description) as TextView
            textView.text = args.description
            Toast.makeText(requireContext(), args.description, Toast.LENGTH_LONG).show()
        }


        // Inflate the layout for this fragment
        return view
    }

    private fun clickManager(view: View) {
        view.capture_btn.setOnClickListener {

            cameraManager()
        }

        view.choose_image.setOnClickListener {
            pickImage()
        }

        view.post_button_found_item.setOnClickListener {

            if (image_uri != null) {  //last opp bilde til database
                uploadImageToDatabase()
            }


            val intent1 = Intent(activity, FrontPage::class.java)
            startActivity(intent1)
        }

        view.image_view.setOnClickListener {
            coverChecker = "cover"
            pickImage()
        }

        view.description.setOnClickListener {

            val description= view?.findViewById(R.id.description) as? TextView
            val descriptionString = description?.text.toString()
            if (descriptionString != null) {

                passData(descriptionString)
            }

            appNavigator.navigateToDescription()
        }
    }

    fun passData(data: String){
        dataPasser.onDescriptionPass(data)
    }

    private fun mapManager() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(OnMapReadyCallback {
            googleMap = it
            //googleMap.isMyLocationEnabled = true
            val location1 = LatLng(62.479386, 6.819220)
            googleMap.addMarker(MarkerOptions().position(location1).title("My location"))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location1, 10f))

            //https://stackoverflow.com/questions/41254834/add-marker-on-google-map-on-touching-the-screen-using-android/41254877
            googleMap.setOnMapClickListener { latLng -> // Creating a marker
                val markerOptions = MarkerOptions()

                // Setting the position for the marker
                markerOptions.position(latLng)

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude.toString() + " : " + latLng.longitude)

                // Clears the previously touched position
                googleMap.clear()

                // Animating to the touched position
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))

                // Placing a marker on the touched position
                googleMap.addMarker(markerOptions)
            }


        })
    }

    private fun cameraManager() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED ||
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                == PackageManager.PERMISSION_DENIED
            ) {
                //Permission was not enabled
                val permission = arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                //show popup ti request permission
                requestPermissions(permission, PERMISSION_CODE)
            } else {
                //Permission already granted
                openCamera()
            }
        } else {
            //systen is us <marhmallow
            openCamera()
        }
    }

    private fun pickImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, RequestCode)
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New picture")
        values.put(MediaStore.Images.Media.TITLE, "From the Camera")
        val resolver: ContentResolver = requireActivity().contentResolver
        image_uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        // called when user presses ALLOW or DENY from Permission Request Popup
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup was granted
                    openCamera()
                } else {
                    //permission from popup was denied
                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)



        if (resultCode == Activity.RESULT_OK) {
            //set image captured to image view
            image_view.setImageURI(image_uri)
            Toast.makeText(requireContext(), "tatt bilde skal funke", Toast.LENGTH_SHORT).show()
        }



        if (RequestCode == RequestCode && resultCode == Activity.RESULT_OK && data!!.data != null) run {
            image_uri = data.data
            image_view.setImageURI(image_uri)

            Toast.makeText(requireContext(), "uploading", Toast.LENGTH_LONG).show()

        }
    }

    private fun uploadImageToDatabase() {
        storageRef = FirebaseStorage.getInstance().reference.child("User Images")

        if (image_uri != null) {
            val fileRef = storageRef!!.child(System.currentTimeMillis().toString() + ".jpg")
            var uploadTask: StorageTask<*>
            uploadTask = fileRef.putFile(image_uri!!)

            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation fileRef.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Bildet er lastet opp", Toast.LENGTH_LONG)
                        .show()

                }

            }
        }
    }

    lateinit var itemDescription: TextView
    lateinit var itemColor: EditText
    lateinit var itemTime: EditText
    lateinit var itemContact: EditText


    private fun UploadAttributesToDB() {
        itemDescription = view?.findViewById(R.id.description) as TextView
        itemColor = view?.findViewById(R.id.colordescription) as EditText
        itemTime = view?.findViewById(R.id.timewhenfound) as EditText
        //ItemLocation = view.findViewById(R.id.l)
        itemContact = view?.findViewById(R.id.contactinformation) as EditText

        if (itemDescription.text.toString().isEmpty()) {
            itemDescription.error = "Plesase enter description"
        }


        val ref = FirebaseDatabase.getInstance().getReference("test")

    }

}