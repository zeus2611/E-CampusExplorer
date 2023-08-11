package com.example.firsttrial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout


class QR_Scan_Unlock : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =
            inflater.inflate(R.layout.fragment_q_r__scan__unlock, container,
                false)
        val button = view.findViewById<Button>(R.id.button11)
        button.setOnClickListener {
            val newFragment = DistanceDuration()
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTrasaction = fragmentManager.beginTransaction()
            fragmentTrasaction.replace(R.id.f1, newFragment)
            fragmentTrasaction.addToBackStack(null)
            fragmentTrasaction.commit()
        }

        val referAndEarnLayout = view.findViewById<ConstraintLayout>(R.id.t1)
        referAndEarnLayout.setOnClickListener {
            // Handle the click event here
            // For example, show a Toast message
//            Toast.makeText(requireContext(), "Blank space clicked", Toast.LENGTH_SHORT).show()
        }

        return view
    }


}