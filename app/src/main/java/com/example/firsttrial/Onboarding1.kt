package com.example.firsttrial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.firsttrial.R


class Onboarding1 : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_onboarding1, container, false)
        val button=view.findViewById<Button>(R.id.button3)
        val button1=view.findViewById<Button>(R.id.button5)
        button .setOnClickListener{
            findNavController().navigate(R.id.onboarding2)

        }
        button1 .setOnClickListener{
            findNavController().navigate(R.id.navtry)

        }
        return view
    }

}
