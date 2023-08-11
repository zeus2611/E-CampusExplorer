package com.example.firsttrial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import com.example.firsttrial.Fragment.FourthhFragment


class Offers : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_offers, container, false)
        val offerlayout = view.findViewById<RelativeLayout>(R.id.offer)

        offerlayout.setOnClickListener {
            // Handle the click event here
            // For example, show a Toast message
//            Toast.makeText(requireContext(), "Blank space clicked", Toast.LENGTH_SHORT).show()
        }

        val button1 = view.findViewById<Button>(R.id.button32)
        button1.setOnClickListener {
            val newFragment = FourthhFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransition = fragmentManager.beginTransaction()
            fragmentTransition.replace(R.id.f4, newFragment)
            fragmentTransition.addToBackStack(null)
            fragmentTransition.commit()

        }
        return view
    }


}