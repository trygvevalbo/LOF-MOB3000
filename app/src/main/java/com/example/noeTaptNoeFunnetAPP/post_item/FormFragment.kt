package com.example.noeTaptNoeFunnetAPP.post_item

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.noeTaptNoeFunnetAPP.FrontPage
import com.example.noeTaptNoeFunnetAPP.R
import com.example.noeTaptNoeFunnetAPP.databinding.FragmentFormBinding
import com.fonfon.kgeohash.GeoHash
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.fragment_form.*
import kotlinx.android.synthetic.main.liste.*
import java.io.File
import java.util.*

/*
    Kilder:
    Kamera og PickImage https://www.youtube.com/watch?v=DPHkhamDoyc og https://www.youtube.com/watch?v=LpL9akTG4hI
    GeoPoint og GeoHash https://www.youtube.com/watch?v=mx1mMdHBi5Q&t og https://github.com/drfonfon/android-kotlin-geohash
    Opplasting av bilde og data https://www.youtube.com/watch?v=BJpxsxCBkcQ&list og https://firebase.google.com/docs/firestore/manage-data/add-data
    Marker https://stackoverflow.com/questions/41254834/add-marker-on-google-map-on-touching-the-screen-using-android
    Lokasjon https://www.youtube.com/watch?v=vard0CUTLbA
 */

class FormFragment : Fragment() {

    private lateinit var appNavigator: AppNavigator //interface til å sende til description fragment

    private var model: FormViewModel?=null


    var geoHashLocation = GeoHash(59.412369, 9.067760, 5)
    var geoPoint = GeoPoint(59.412369, 9.067760)
    var currentGeoPoint = GeoPoint(59.412369, 9.067760)
    var currentTimeStamp = System.currentTimeMillis() / 1000L
    var selectedLocation: LatLng = LatLng(59.412369, 9.067760)
    var userLocation: LatLng? = null

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest : LocationRequest
    lateinit var googleMap: GoogleMap
    private val FILE_NAME = "photo.jpg"
    private lateinit var photoFile: File


    private val IMAGE_CAPTURE_CODE = 1001
    private val PERMISSION_CODE = 2000
    private val RequestCode = 42
    var image_uri: Uri? = Uri.EMPTY
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
    ): View {


        val binding = DataBindingUtil.inflate<FragmentFormBinding>(
            inflater, R.layout.fragment_form,
            container, false
        )
        model= ViewModelProviders.of(requireActivity()).get(FormViewModel::class.java)
        binding.lifecycleOwner = activity

       binding.viewModel = model//koble ViewModel Til fragment_form.xml


        model!!.savedDescription.observe(viewLifecycleOwner,
            { o -> binding.description.text = o!!.toString() }) //motta description



        //set Headers
        if(model!!.postType=="Funnet") {
            binding.timewhenfound.hint ="Når var den funnet"
            binding.postButtonFoundItem.text="Post funnet gjenstand"
        }else{
            binding.timewhenfound.hint ="Når var den mistet"
            binding.postButtonFoundItem.text="Post tapt gjenstand"
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
                    googleMap.addMarker(selectedLocation.let { it1 ->
                        MarkerOptions().position(it1)
                    })
                } else if (model.userLatitude != null) {
                    userLocation = LatLng(
                        model.userLatitude!!,
                        model.userLongitude!!
                    ) // hent det bruker har skrevet inn
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10F))

                } else {
                    getNewLocation()
                    selectedLocation = LatLng(currentGeoPoint.latitude, currentGeoPoint.latitude)  // bare bruk lokasjon til bruker
                }
            }

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, 10f))

            googleMap.setOnMapClickListener {
                appNavigator.navigateToMapFullScreen()

            }

        })
    }
    // Få inn lokasjon for GeoPoint og GeoHash
    private fun getGeoLocation () {

        val location = Location("geohash")
        location.latitude =  selectedLocation.latitude
        location.longitude = selectedLocation.longitude

        geoHashLocation = GeoHash(location, 5)

        geoPoint = GeoPoint(location.latitude, location.longitude)
    }
    @SuppressLint("MissingPermission")
    private fun getNewLocation(){
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 2
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest, locationCallback, Looper.myLooper()
        )
    }
    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            var lastLocation =p0.lastLocation
            val location = Location("geohash")
            location.latitude = lastLocation.latitude
            location.longitude = lastLocation.longitude

            geoHashLocation = GeoHash(location, 5)
            currentGeoPoint = GeoPoint(lastLocation.latitude, lastLocation.longitude)

        }
    }

    // Permission sjekk
    private fun cameraManager() {
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
                Manifest.permission.CAMERA
            )
            //show popup ti request permission
            requestPermissions(permission, PERMISSION_CODE)
        } else {
            //Permission already granted
            openCamera()
        }

        }
    // Kamera funksjon med versjonssjekk
    private fun openCamera(){
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.P) {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = photoFile(FILE_NAME)
            val context: Context? = activity
            val packageManager = context!!.packageManager

            val fileProvider = FileProvider.getUriForFile(
                context,
                "com.example.noeTaptNoeFunnetAPP.fileprovider",
                photoFile
            )
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                startActivityForResult(takePictureIntent, RequestCode)
            } else {
                Toast.makeText(
                    activity,
                    "This device does not have a camera.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "New picture")
            values.put(MediaStore.Images.Media.TITLE, "From the Camera")
            val resolver: ContentResolver = requireActivity().contentResolver
            image_uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!

            //camera intent
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
            startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
        }
    }


    // PhotoFile Definisjon
    private fun photoFile(fileName: String): File {
        val storeageDirectory = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storeageDirectory)
    }


    private fun pickImage() {
        //Intent to pick image
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_DENIED ||
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            == PackageManager.PERMISSION_DENIED
        ) {
            //Permission was not enabled
            val permission = arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            //show popup ti request permission
            requestPermissions(permission, IMAGE_PICK_CODE)
        } else {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
        }
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        // called when user presses ALLOW or DENY from Permission Request Popup
        if (requestCode === PERMISSION_CODE) {

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
            else if (requestCode === IMAGE_PICK_CODE ) {


                        if (grantResults.isNotEmpty() && grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED
                        ) {
                            //permission from popup was granted
                            pickImage();
                        } else {
                            //permission from popup was denied
                            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
                        }

            }
            else  {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    // Resultatsfunksjon
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.P){

            if (requestCode == RequestCode && resultCode == Activity.RESULT_OK) {
                image_uri = Uri.fromFile(photoFile)

                if (data != null) {
                    model?.image?.value = data.data
                }
                image.setImageURI(image_uri)
                model!!.setImage(image_uri) // set verdi

            } else if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
                if (data != null) {
                    image_uri = data.data!!
                    model?.image?.value = data.data
                }
                image.setImageURI(image_uri)
                model!!.setImage(image_uri) // set verdi

            }
            else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }else if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_CAPTURE_CODE) {
            if (data != null) {
                model?.image?.value = data.data
            }
            image.setImageURI(image_uri)
            model!!.setImage(image_uri)
        } else if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            if (data != null) {
                image_uri = data.data
                model?.image?.value = data.data
            }

            image.setImageURI(image_uri)
            model!!.setImage(image_uri)
        }else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun checkForm(binding: FragmentFormBinding, model: FormViewModel?): Boolean {
        if (model!!.image.value== null) {
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
        }else if (binding.timewhenfound.text.toString().isEmpty()) {
            binding.timewhenfound.error = "Venligst tast inn tidspunkt"
            binding.timewhenfound.requestFocus()
            return false
        }  else if (selectedLocation == LatLng(59.412369, 9.067760) && geoHashLocation == GeoHash(59.412369, 9.067760, 5) &&  geoPoint == GeoPoint(59.412369, 9.067760) || selectedLocation == null) {
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

                     model.downloadUrl = task.result.toString()
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




    private fun getCurrentUnixTime () {
        currentTimeStamp = System.currentTimeMillis() / 1000L
    }


    private fun uploadTextToDatabase(binding: FragmentFormBinding, model: FormViewModel?) {
        getGeoLocation()
        getCurrentUnixTime()
        val keyWords = arrayOf(
            binding.viewModel?.savedNameItem?.value.toString().trim()
                .toLowerCase(Locale.getDefault()),
            binding.viewModel?.savedDescription?.value.toString().trim()
                .toLowerCase(Locale.getDefault()),
            binding.viewModel?.savedColor?.value.toString().trim().toLowerCase(Locale.getDefault()),
            binding.viewModel?.postType.toString().trim().toLowerCase(Locale.getDefault())
        )
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
        postmap["geoHash"] = geoHashLocation.toString()
        postmap["geopoint"] = geoPoint
        postmap["timeStamp"] = currentTimeStamp.toString()


        val mFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
        if(checkForm(binding, model)){
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