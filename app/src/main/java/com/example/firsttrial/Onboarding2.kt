package com.example.firsttrial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController


class Onboarding2 : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_onboarding2, container, false)
        val button=view.findViewById<Button>(R.id.button4)
        val button1=view.findViewById<Button>(R.id.button2)
        button .setOnClickListener{
            findNavController().navigate(R.id.onboarding3)

        }
        button1 .setOnClickListener{
            findNavController().navigate(R.id.navtry)

        }
        return view
    }

}