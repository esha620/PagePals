package com.example.pagepals1.fragments.location

import android.content.pm.PackageManager
import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
//import com.example.pagepals1.Manifest
import com.example.pagepals1.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import java.util.Locale


class LocationFragment : Fragment() {

    private lateinit var locationTitle: TextView
    private lateinit var locationBtn: Button
    private lateinit var locationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_location, container, false)

        locationTitle = view.findViewById(R.id.locationTitle)!!
        locationBtn = view.findViewById(R.id.getLocationButton)!!
        locationClient = LocationServices.getFusedLocationProviderClient(requireActivity()) // get location client

        locationBtn.setOnClickListener {
            // make sure the user has given permission to access their location
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // now get the location
                getLocation()
            } else {
                // permission not granted
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
                Toast.makeText(activity,"Location permission not granted", Toast.LENGTH_LONG).show()
            }
        }
        return view
    }


    private fun getLocation() {
        var locManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // check that gps or network is enabled
        if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER)) {
            // more permission checks
            if (ActivityCompat.checkSelfPermission(
                    this.requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this.requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) { return }

            locationClient.getLastLocation().addOnCompleteListener(OnCompleteListener<Location> { task ->

                var location: Location = task.getResult()
                var geocoder = Geocoder(requireActivity(), Locale.getDefault())

                if (location != null) {
                    var addresses: MutableList<Address>? = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1)
                    if (addresses != null) {
                        locationTitle.setText("Authors in ${addresses.get(0).getLocality()}")
                    }
                } else {
                    var locRequest: LocationRequest = LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(10000).setNumUpdates(1)
                    var locCallback: LocationCallback = object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            locationTitle.setText("Authors in your area")
                        }
                    }
                    locationClient.requestLocationUpdates(locRequest, locCallback, Looper.myLooper())
                }
        })

    } else {
        // location services not enabled
        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    }
}