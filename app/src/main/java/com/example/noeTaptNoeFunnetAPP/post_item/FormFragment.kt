package com.example.noeTaptNoeFunnetAPP.post_item

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
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
import androidx.fragment.app.Fragment
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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_form.*
import kotlinx.android.synthetic.main.fragment_form.view.*


class FormFragment : Fragment() {

    private val IMAGE_CAPTURE_CODE  = 1001 // camera funksjon https://www.youtube.com/watch?v=3gkAoF90RZ4
    private val PERMISSION_CODE  = 1000;
    private val RequestCode = 438
    var image_uri: Uri? = null
    private var storageRef: StorageReference? = null

    private var coverChecker: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_form, container, false)
        /*val binding = DataBindingUtil.inflate<FragmentFormBinding>(
            inflater, R.layout.fragment_form, container, false
        )



        binding.form = this

         */
        storageRef = FirebaseStorage.getInstance().reference.child("User Images")

        //val view.capture_btn = getView()?.findViewById(R.id.capture_btn) as ImageView
        view.capture_btn.setOnClickListener {

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
            }
            else{
                //systen is us <marhmallow
                openCamera()
            }
        }





        // val choose_image = getView()?.findViewById(R.id.choose_image) as ImageView
        view.choose_image.setOnClickListener {
            pickImage()
        }









/*        post_button_found_item.setOnClickListener{

            if(image_uri != null) {  //last opp bilde til database
                uploadImageToDatabase()
            }
            val intent1 = Intent(activity, FrontPage::class.java)
            startActivity(intent1)
        }*/

            view.image_view.setOnClickListener {
            coverChecker = "cover"
            pickImage()
        }

       /* description.setOnClickListener {
            var mFragment: Fragment? = null
            mFragment = DescriptionFragment()
            val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, mFragment!!).commit()


        } */





        // Inflate the layout for this fragment
        return view
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
        when(requestCode){
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
        if(resultCode == Activity.RESULT_OK){
            //set image captured to image view
            image_view.setImageURI(image_uri)
            Toast.makeText(requireContext(), "tatt bilde skal funke", Toast.LENGTH_SHORT).show()
        }

        if(RequestCode == RequestCode && resultCode == Activity.RESULT_OK && data!!.data != null) run {
            image_uri = data.data
            image_view.setImageURI(image_uri)
            Toast.makeText(requireContext(), "uploading", Toast.LENGTH_LONG).show()

        }
    }

    private fun uploadImageToDatabase() {


        if(image_uri!= null){
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
                if(task.isSuccessful){
                    Toast.makeText(requireContext(), "Bildet er lastet opp", Toast.LENGTH_LONG).show()

                }

            }
        }
    }


    private fun pickImage(){
        val intent  = Intent()
        intent.type="image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, RequestCode)
    }



}