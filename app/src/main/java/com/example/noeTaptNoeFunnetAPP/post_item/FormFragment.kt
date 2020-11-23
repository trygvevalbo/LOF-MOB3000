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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
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
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.fragment_form.*
import kotlinx.android.synthetic.main.liste.*
import java.util.*


class FormFragment : Fragment() {

    private lateinit var appNavigator: AppNavigator //interface til å sende til description fragment

    private var model: FormViewModel?=null

    lateinit var googleMap: GoogleMap

    var selectedLocation: LatLng? = null

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


        val binding = DataBindingUtil.inflate<FragmentFormBinding>(
            inflater, R.layout.fragment_form,
            container, false
        )
        model= ViewModelProviders.of(requireActivity()).get(FormViewModel::class.java)
        binding.lifecycleOwner = activity

       binding.viewModel = model//attach viewModel to xml


        model!!.savedDescription.observe(viewLifecycleOwner,
            { o -> binding.description.text = o!!.toString() }) //motta description



        //set correct headings for type of post
        if(model!!.postType=="Funnet") {
            binding.timewhenfound.hint ="Når var den funnet"
        }else{
            binding.timewhenfound.hint ="Når var den mistet"
        }

        mapManager(model)


        clickManager(binding)

        if(model?.downloadUrl != null) {

            activity?.let { Glide.with(it).load(model?.downloadUrl).into(binding.image) }

        }

        return binding.root
    }


    private fun clickManager(binding: FragmentFormBinding) {
        binding.captureBtn.setOnClickListener {

            cameraManager()
        }

        binding.chooseImage.setOnClickListener {
            pickImage()
        }

        binding.image.setOnClickListener {
            coverChecker = "cover"
            pickImage()
        }

        binding.postButtonFoundItem.setOnClickListener {
            if (model?.image?.value  != null) {  //last opp bilde til database
                uploadImageToDatabase(binding, model)
           }else{
                uploadTextToDatabase(binding, model)
            }
        }


        binding.description.setOnClickListener {
            appNavigator.navigateToDescription() // naviger til fragmentDescription
        }

        binding.timewhenfound.setOnClickListener {
            appNavigator.navigateToSelectDate()
        }
    }

    private fun mapManager(model: FormViewModel?) {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?


        mapFragment?.getMapAsync(OnMapReadyCallback {
            googleMap = it
            if (model != null) {
                if (model.savedLatitude.value != null && model.savedLongitude.value != null) {
                    selectedLocation = LatLng(
                        model.savedLatitude.value!!,
                        model.savedLongitude.value!!
                    ) // hent det bruker har skrevet inn
                    googleMap.addMarker(selectedLocation?.let { it1 ->
                        MarkerOptions().position(it1)
                    })
                } else if(model.userLatitude != null){
                    selectedLocation = LatLng(
                        model.userLatitude!!,
                        model.userLongitude!!
                    ) // hent det bruker har skrevet inn
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, 10F))

                } else {
                    selectedLocation = LatLng(59.412369, 9.067760) // bare bruk lokasjon til bruker
                }
            }

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, 10f))

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
            image.setImageURI(image_uri)
            model!!.setImage(image_uri) // set verdi

        }

        if (RequestCode == RequestCode && resultCode == Activity.RESULT_OK && data!!.data != null) run {
            image_uri = data.data
            model?.image?.value = data.data
            image.setImageURI(image_uri)
            model!!.setImage(image_uri)

        }


    }

    private fun checkForm(binding: FragmentFormBinding, model: FormViewModel?): Boolean {
        if (model?.image?.value.toString().isNullOrEmpty()) {
            binding.nameOfItem.error = "Venligst last opp et bilde av tingen"
            binding.nameOfItem.requestFocus()
            return false
        } else if (binding.nameOfItem.text.toString().isNullOrEmpty()) {
            binding.nameOfItem.error = "Venligst tast inn navnet på tingen"
            binding.nameOfItem.requestFocus()
            return false
        } else if (binding.description.text.toString().isNullOrEmpty()) {
            binding.description.error = "Venligst tast inn en beskrivelse"
            binding.description.requestFocus()
            return false
        } else if (binding.colordescription.text.toString().isNullOrEmpty()) {
            binding.colordescription.error = "Venligst tast inn en fargebeskrivelse på tingen"
            binding.colordescription.requestFocus()
            return false
        }else if (binding.timewhenfound.text.toString().isNullOrEmpty()) {
            binding.timewhenfound.error = "Venligst tast inn tidspunkt"
            binding.timewhenfound.requestFocus()
            return false
        } else if (binding.contactinformation.text.toString().isNullOrEmpty()) {
            binding.contactinformation.error = "Venligst tast inn mobilnummer eller E-post"
            binding.contactinformation.requestFocus()
            return false
        } else if (selectedLocation == LatLng(59.412369, 9.067760)) {
            binding.contactinformation.error = "Venligst velg hvor den var funnet"
            binding.contactinformation.requestFocus()
            return false
        } else{
            return true
        }

    }

    private fun uploadImageToDatabase(binding: FragmentFormBinding, model: FormViewModel?) {
        storageRef = FirebaseStorage.getInstance().reference.child("PostImages")

        if (model?.image?.value  != null && !model?.image?.value.toString().contains("http")) { //dersom bruker ønsker å ikke endre bilde så laster vi ikke opp på nytt
             primaryKey =  UUID.randomUUID().toString()
            val fileRef = storageRef!!.child("$primaryKey.jpg")
            var uploadTask: StorageTask<*>
            uploadTask = fileRef.putFile(model?.image?.value!!)

            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {

                        throw it
                    }

                }
                return@Continuation fileRef.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                     model?.downloadUrl = task.result.toString()
                    uploadTextToDatabase(binding, model)

                }
                else{
                    Toast.makeText(
                        requireContext(),
                        "Feilet med å laste opp bilde",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }else{
            uploadTextToDatabase(binding, model)
        }
    }

    private fun uploadTextToDatabase(binding: FragmentFormBinding, model: FormViewModel?) {

        val keyWords = arrayOf(
            binding.viewModel?.savedNameItem?.value.toString().trim().toLowerCase(Locale.getDefault()),
            binding.viewModel?.savedDescription?.value.toString().trim().toLowerCase(Locale.getDefault()),
            binding.viewModel?.savedColor?.value.toString().trim().toLowerCase(Locale.getDefault()),
            binding.viewModel?.postType.toString().trim().toLowerCase(Locale.getDefault()))
        val postmap = HashMap<String, Any>()

        postmap["postImage"] = binding.viewModel?.downloadUrl.toString()
        postmap["itemName"] = binding.viewModel?.savedNameItem?.value.toString()
        postmap["itemDesk"] = binding.viewModel?.savedDescription?.value.toString()
        postmap["itemColor"] = binding.viewModel?.savedColor?.value.toString()
        postmap["postTime"] = binding.viewModel?.savedTime?.value.toString()
        postmap["itemLat"] = binding.viewModel?.savedLatitude?.value.toString()
        postmap["itemLng"] = binding.viewModel?.savedLongitude?.value.toString()
        postmap["postType"] = binding.viewModel?.postType.toString()
        postmap["postContact"] = binding.viewModel?.savedContact?.value.toString()
        postmap["userEmail"]= binding.viewModel?.userEmail?.value.toString()
        postmap["keyWords"] = Arrays.asList(*keyWords)

        postmap["keyWords"] = listOf(*keyWords)

        val mFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
        if(checkForm(binding,model)){
            if(model?.documentId.isNullOrEmpty()) {
                mFirestore.collection("Posts").add(postmap).addOnSuccessListener() { documents ->

                    Toast.makeText(requireContext(), "Post laget", Toast.LENGTH_SHORT).show()
                    val intent1 = Intent(activity, FrontPage::class.java)
                    startActivity(intent1)

                }.addOnFailureListener() {
                    Toast.makeText(
                        requireContext(),
                        "Noe er feil! venligst prøv igjen",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }else{
                model?.documentId?.let {
                    mFirestore.collection("Posts").document(it)
                        .update(postmap)
                }?.addOnSuccessListener() { documents ->

                    Toast.makeText(requireContext(), "Post laget", Toast.LENGTH_SHORT).show()
                    val intent1 = Intent(activity, FrontPage::class.java)
                    startActivity(intent1)

                }?.addOnFailureListener() {
                    Toast.makeText(
                        requireContext(),
                        "Noe er feil! venligst prøv igjen",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }

}