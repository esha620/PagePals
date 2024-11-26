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
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pagepals1.R
import com.example.pagepals1.data.BookClubViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import java.util.Locale


class LocationFragment : Fragment() {

    private lateinit var locationTitle: TextView
    private lateinit var locationBtn: Button
    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var currentCity: String
    private lateinit var mBookClubViewModel: BookClubViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LocListAdapter
    private lateinit var alert: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_location, container, false)

        mBookClubViewModel = ViewModelProvider(this).get(BookClubViewModel::class.java)
        locationTitle = view.findViewById(R.id.locationTitle)!!
        locationBtn = view.findViewById(R.id.getLocationButton)!!
        locationClient = LocationServices.getFusedLocationProviderClient(requireActivity()) // get location client
        // create recycler view to show clubs in my city
        recyclerView = view.findViewById(R.id.locRecycler)
        adapter = LocListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // make a dialog box to say "No clubs in your area" if there's no results
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("No clubs found in your area")
            .setCancelable(true) // Allows the dialog to be dismissed by tapping outside
            .setPositiveButton("Dismiss") { dialog, _ ->
                // Action for dismiss button
                dialog.dismiss()
            }
        alert = builder.create()

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
                        currentCity = addresses.get(0).getLocality()
                        locationTitle.setText("Clubs in ${addresses.get(0).getLocality()}")
                        showLocalClubs()
                    }
                } else {
                    var locRequest: LocationRequest = LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(10000).setNumUpdates(1)
                    var locCallback: LocationCallback = object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            location = locationResult.getLastLocation()
                            var addresses: MutableList<Address>? = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1)
                            if (addresses != null)
                                locationTitle.setText("Clubs in ${addresses.get(0).getLocality()}")
                            if (addresses != null) {
                                currentCity = addresses.get(0).getLocality()
                            }
                        }
                        override fun onLocationAvailability(locationAvailability: com.google.android.gms.location.LocationAvailability?) {
                            super.onLocationAvailability(locationAvailability)
                            if (locationAvailability != null && !locationAvailability.isLocationAvailable) {
                                Toast.makeText(requireContext(), "GPS signal lost. Please wait for a signal.", Toast.LENGTH_SHORT).show()
                            }
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

    private fun showLocalClubs() {
        adapter.clearData()
        // get all clubs in the current city
        mBookClubViewModel.readAllData.observe(viewLifecycleOwner, Observer { club ->
            for (club in club) {
                if (club.city == currentCity) {
                    adapter.setData(club)
                }
            }
            if (adapter.itemCount == 0) {
                alert.show()
            }
        })

    }


}