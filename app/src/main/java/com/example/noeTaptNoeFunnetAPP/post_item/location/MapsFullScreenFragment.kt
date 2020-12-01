package com.example.noeTaptNoeFunnetAPP.post_item.location

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.noeTaptNoeFunnetAPP.R
import com.example.noeTaptNoeFunnetAPP.databinding.FragmentMapsFullScreenBinding
import com.example.noeTaptNoeFunnetAPP.post_item.AppNavigator
import com.example.noeTaptNoeFunnetAPP.post_item.FormViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

/*
    Kilder:
    Get marker https://stackoverflow.com/questions/41254834/add-marker-on-google-map-on-touching-the-screen-using-android
 */

class MapsFullScreenFragment : Fragment() {



    //Location Variabler
    private lateinit var mMap: GoogleMap
    private lateinit var appNavigator: AppNavigator
    var selectedLocation: LatLng? = null
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var userLatLng: LatLng? = null

    private var model: FormViewModel?=null



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

        binding.viewModel = model

        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment?
        mapFragment?.getMapAsync {
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
            mMap.setOnMapClickListener { latLng -> // Marker
                val markerOptions = MarkerOptions()



                binding.viewModel?.setLocation(latLng.latitude, latLng.longitude)


                // Sett markeren for lokasjonen
                markerOptions.position(latLng)

                // Sett tittel
                // Kommer opp når brukeren 'tapper' på en lokasjon på kartet
                markerOptions.title(latLng.latitude.toString() + " : " + latLng.longitude)

                // sletter forrige lokasjon
                mMap.clear()

                // Animerer trykket på lokasjon
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))

                // Plaserer markør på trykket på lokasjon

                mMap.addMarker(markerOptions)

            }
        }

        binding.locationDone.setOnClickListener {
            appNavigator.navigateFromMapToForm()
        }
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appNavigator = context as AppNavigator

    }

}