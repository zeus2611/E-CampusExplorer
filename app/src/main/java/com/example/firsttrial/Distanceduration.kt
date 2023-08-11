package com.example.firsttrial

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class DistanceDuration : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: Location? = null
    private var totalDistance: Double = 0.0

    private var finalDistance: Double = 0.0
    private var distance1: Double = finalDistance

    private var elapsedTimeInSeconds: Long = 0L // Updated variable for elapsed time

    private var startTime: Long = 0L
    private lateinit var distanceTextView: TextView
    private lateinit var durationTextView: TextView
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private lateinit var stopButton: Button
    private var isTrackingEnabled: Boolean = true

    private lateinit var pay: Button

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 123
        private const val MIN_TIME_INTERVAL = 2000L // Minimum time interval between location updates in milliseconds
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.fragment_distanceduration, container, false)

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as
                    SupportMapFragment
        mapFragment.getMapAsync(this)

        distanceTextView = view.findViewById(R.id.distanceTextView)
        durationTextView = view.findViewById(R.id.durationTextView)
        stopButton = view.findViewById(R.id.stopButton)
        pay= view.findViewById(R.id.button34)
        pay.visibility = View.GONE

        stopButton.setOnClickListener {
            stopTracking()
            pay.visibility = View.VISIBLE
        }

        handler = Handler()

        startTracking()



        val distance : Double = finalDistance       //5 Rs. per km
//        val distance1: Double = (finalDistance)       //5 Rs. per km
        pay.setOnClickListener{
            val intent = Intent(requireContext(), payNow::class.java)
            intent.putExtra("distance" , finalDistance )
            intent.putExtra("distance1" , distance1 )
            startActivity(intent)
        }

        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Disable the default behavior of the map when a marker is clicked
        mMap.setOnMarkerClickListener { true }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Request location permission
            return
        }
        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
    }

    private fun startTracking() {
        if (isTrackingEnabled && ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        currentLocation = location
                        mMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    location.latitude,
                                    location.longitude
                                ), 15f
                            )
                        )

                        startTime = SystemClock.elapsedRealtime()
                        runnable = object : Runnable {
                            override fun run() {
                                updateDistanceAndDuration()
                                handler.postDelayed(this, MIN_TIME_INTERVAL) // Update every minimum time interval
                            }
                        }
                        handler.postDelayed(runnable, MIN_TIME_INTERVAL) // Start updating
                    }
                }
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun updateDistanceAndDuration() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Request location permission
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val previousLocation = currentLocation
                    currentLocation = location

                    previousLocation?.let { prev ->
                        val distance = prev.distanceTo(location
                        ).toDouble() // Distance in meters
                        totalDistance += distance / 1000 // Convert meters to kilometers
                        distanceTextView.text = "Distance: %.2f km".format(totalDistance)
                    }

                    val elapsedTime = SystemClock.elapsedRealtime() - startTime
                    val hours = (elapsedTime / 3600000) % 24
                    val minutes = (elapsedTime / 60000) % 60
                    val seconds = (elapsedTime / 1000) % 60
                    durationTextView.text = "Duration: %02d:%02d:%02d".format(hours, minutes, seconds)

                    mMap.clear() // Clear previous markers
                    mMap.addMarker(
                        MarkerOptions().position(LatLng(location.latitude, location.longitude))
                            .title("Current Location")
                    )
                }
            }
    }

    private fun updateDuration() {
        val currentTime = SystemClock.elapsedRealtime()
        elapsedTimeInSeconds = ((currentTime - startTime) / 1000)
        durationTextView.text = "Duration: %02d:%02d:%02d".format(
            elapsedTimeInSeconds / 3600,
            (elapsedTimeInSeconds % 3600) / 60,
            elapsedTimeInSeconds % 60
        )
    }

    private fun stopTracking() {
        isTrackingEnabled = false
        handler.removeCallbacks(runnable)
        finalDistance = totalDistance // Save the totalDistance as the finalDistance
        stopButton.visibility = View.GONE

    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }


    override fun onResume() {
        super.onResume()
        if (isTrackingEnabled && currentLocation != null) {
            handler.postDelayed(runnable, MIN_TIME_INTERVAL)
        }
    }
    // Function to retrieve the final distance from outside the class
    fun getFinalDistance(): Double {
        return finalDistance
    }
}
