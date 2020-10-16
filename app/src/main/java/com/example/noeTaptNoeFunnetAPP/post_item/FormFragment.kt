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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.noeTaptNoeFunnetAPP.FrontPage
import com.example.noeTaptNoeFunnetAPP.R
import com.example.noeTaptNoeFunnetAPP.databinding.FragmentFormBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_form.*
import kotlinx.android.synthetic.main.fragment_form.view.*
import java.util.*



class FormFragment : Fragment() {

    private var mUri: Uri? = null //deklarasjon av url til bilde

    private lateinit var appNavigator: AppNavigator //interface til å sende til description fragment

    private var model: FormViewModel?=null

    lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap

    private val IMAGE_CAPTURE_CODE = 1001 // camera funksjon https://www.youtube.com/watch?v=3gkAoF90RZ4
    private val PERMISSION_CODE = 1000
    private val RequestCode = 438
    var image_uri: Uri? = null
    private var storageRef: StorageReference? = null
    private var coverChecker: String? = ""

    private var primaryKey :String? =""





    override fun onAttach(context: Context) { //få context til å senere kunne sende til deskription
        super.onAttach(context)
        appNavigator = context as AppNavigator
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = activity?.run {
            ViewModelProviders.of(this)[FormViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //val view = inflater.inflate(R.layout.fragment_form, container, false)

        val binding = DataBindingUtil.inflate<FragmentFormBinding>(inflater, R.layout.fragment_form,
            container, false)

       // binding.viewModel = model//attach your viewModel to xml

        if (savedInstanceState != null) {
            mUri = savedInstanceState.getParcelable("uri")
            image_view.setImageURI(mUri)
        }

        mapManager()


        clickManager(binding)

        setFormValues(model)

        model= ViewModelProviders.of(requireActivity()).get(FormViewModel::class.java)


        model!!.savedDescription.observe(viewLifecycleOwner,
            { o -> binding.description.text = o!!.toString() }) //motta description

        model!!.savedNameItem.observe(viewLifecycleOwner,
            { o -> binding.nameOfItem?.setText(o!!.toString())}) // ta imot husket verdi for beskrivelse






        return binding.root
    }

   private fun setFormValues(model: FormViewModel){

   }




    private fun clickManager(binding: FragmentFormBinding) {
        binding.captureBtn.setOnClickListener {

            cameraManager()
        }

        binding.chooseImage.setOnClickListener {
            pickImage()
        }

        binding.postButtonFoundItem.setOnClickListener {
            if (image_uri != null) {  //last opp bilde til database
                uploadImageToDatabase()
            }
            uploadTextToDatabase()

            val intent1 = Intent(activity, FrontPage::class.java)
            startActivity(intent1)
        }

        binding.imageView.setOnClickListener {
            coverChecker = "cover"
            pickImage()
        }



        binding.description.setOnClickListener {

            appNavigator.navigateToDescription() // naviger til fragmentDescription

        }

        binding.timewhenfound.setOnClickListener {

            appNavigator.navigateToSelectDate()
        }
    }





    private fun mapManager() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(OnMapReadyCallback {
            googleMap = it


                val location1 = LatLng(62.479386, 6.819220)
                googleMap.addMarker(MarkerOptions().position(location1).title("My location"))
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location1, 10f))

                googleMap.setOnMapClickListener {
                    appNavigator.navigateToMapFullScreen()

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
        }



        if (RequestCode == RequestCode && resultCode == Activity.RESULT_OK && data!!.data != null) run {
            image_uri = data.data
            image_view.setImageURI(image_uri)
        }
    }

    private fun uploadImageToDatabase() {
        storageRef = FirebaseStorage.getInstance().reference.child("PostImages")

        if (image_uri != null) {
             primaryKey =  UUID.randomUUID().toString()
            val fileRef = storageRef!!.child("$primaryKey.jpg")
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

                    Toast.makeText(requireContext(), primaryKey, Toast.LENGTH_LONG)
                        .show()

                }

            }
        }
    }
    lateinit var itemName: EditText
    lateinit var itemDescription: TextView
    lateinit var itemColor: EditText
    lateinit var itemTime: EditText
    lateinit var itemContact: EditText


    private fun uploadTextToDatabase() {
        itemName= view?.findViewById(R.id.nameOfItem) as EditText
        itemDescription = view?.findViewById(R.id.description) as TextView
        itemColor = view?.findViewById(R.id.colordescription) as EditText
        //itemTime = view?.findViewById(R.id.timewhenfound) as EditText
        //ItemLocation = view.findViewById(R.id.l)
        itemContact = view?.findViewById(R.id.contactinformation) as EditText

        val nameOfItem = itemName.text.toString()
        val descriptionOfFound = itemDescription.text.toString()
        val colorOfFound = itemColor.text.toString()
//        val time = itemTime.text.toString()
       // val lat = itemLocation.text.toString
       // val long = itemLat.text.toString()
       // val brukernavn=
        val typeOfPost = "FoundItem"

        val contact = itemContact.text.toString()



        var database = FirebaseDatabase.getInstance().reference.child("Posts")

        if(primaryKey==""){
            primaryKey =  UUID.randomUUID().toString()
        }
            primaryKey?.let {
                database.child(it).setValue(
                    FormValue(
                        nameOfItem,
                        descriptionOfFound,
                        colorOfFound,
                        contact,
                        typeOfPost
                    )
                )
            }

    }

}