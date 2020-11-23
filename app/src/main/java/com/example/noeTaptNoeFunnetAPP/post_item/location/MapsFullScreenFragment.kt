package com.example.noeTaptNoeFunnetAPP.post_item.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.noeTaptNoeFunnetAPP.R
import com.example.noeTaptNoeFunnetAPP.databinding.FragmentFormBinding
import com.example.noeTaptNoeFunnetAPP.databinding.FragmentMapsFullScreenBinding
import com.example.noeTaptNoeFunnetAPP.post_item.AppNavigator
import com.example.noeTaptNoeFunnetAPP.post_item.FormViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_description.view.*


class MapsFullScreenFragment : Fragment() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest : LocationRequest
    private var itemLocation : LatLng? = null
    var selectedLocation: LatLng? = null

    private lateinit var mMap: GoogleMap
    private var PERMISSION_ID  : Int= 1000

    private lateinit var appNavigator: AppNavigator

    private var model: FormViewModel?=null
    var userLatLng: LatLng? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }




    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentMapsFullScreenBinding>(inflater, R.layout.fragment_maps_full_screen,
            container, false)
        model= ViewModelProviders.of(requireActivity()).get(FormViewModel::class.java)
        binding.lifecycleOwner = activity

        binding.viewModel = model//attach your viewModel to xml

        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment?
        mapFragment?.getMapAsync {
            //https://stackoverflow.com/questions/41254834/add-marker-on-google-map-on-touching-the-screen-using-android/41254877
            mMap = it
            val mapSettings = mMap?.uiSettings
            userLatLng = model!!.userLatitude?.let { it1 ->
                model!!.userLongitude?.let { it2 ->
                    LatLng(
                        it1,
                        it2
                    )
                }
            }
            if (model!!.savedLatitude.value != null) {
                selectedLocation = LatLng(
                    model!!.savedLatitude.value!!,
                    model!!.savedLongitude.value!!
                )
                mMap.addMarker(selectedLocation?.let { it1 ->
                    MarkerOptions().position(it1)
                })
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, 10F))
                if (userLatLng != null) {
                    mMap.isMyLocationEnabled = true
                }
            } else if (userLatLng != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 10f))
                mMap.isMyLocationEnabled = true
            } else {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(59.412369, 9.067760), 10F))
                Toast.makeText(
                    requireContext(),
                    "Venligst godta deling av lokasjon",
                    Toast.LENGTH_LONG
                ).show()
            }


            mapSettings?.isZoomControlsEnabled = true
            mMap.setOnMapClickListener { latLng -> // Creating a marker
                val markerOptions = MarkerOptions()



                binding.viewModel?.setLocation(latLng.latitude, latLng.longitude)


                // Setting the position for the marker
                markerOptions.position(latLng)

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude.toString() + " : " + latLng.longitude)

                // Clears the previously touched position
                mMap.clear()

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))

                // Placing a marker on the touched position

                mMap.addMarker(markerOptions)

            }
        }

        binding.locationDone.setOnClickListener {
            //itemLocation?.let { passData(it) }
            appNavigator.navigateFromMapToForm()
        }
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appNavigator = context as AppNavigator

    }

}