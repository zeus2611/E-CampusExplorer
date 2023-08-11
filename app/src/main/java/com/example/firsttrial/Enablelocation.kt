package com.example.firsttrial

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.firsttrial.R
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import android.widget.Toast



class Enablelocation : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_enablelocation, container, false)
        val button=view.findViewById<Button>(R.id.button1)
        button .setOnClickListener{
            findNavController().navigate(R.id.onboarding1)

        }
        val locationButton = view.findViewById<Button>(R.id.button1)
        locationButton.setOnClickListener {
            enableLocation()
            val button=view.findViewById<Button>(R.id.button1)
            button .setOnClickListener{
                findNavController().navigate(R.id.onboarding1)

            }
        }

        return view

    }

    private fun enableLocation() {
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // GPS is already enabled
            Toast.makeText(requireContext(), "Location is already enabled", Toast.LENGTH_SHORT).show()
        } else {
            // GPS is not enabled, prompt the user to enable it
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
    }

}
