package com.example.firsttrial

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import com.example.firsttrial.Fragment.FourthhFragment


class referAndEarn : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_refer_and_earn, container, false)

        val button = view.findViewById<Button>(R.id.button23)
        button.setOnClickListener {
            val newFragment = FourthhFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransition = fragmentManager.beginTransaction()
            fragmentTransition.replace(R.id.f4, newFragment)
            fragmentTransition.addToBackStack(null)
            fragmentTransition.commit()
        }

        val button1 = view.findViewById<Button>(R.id.button22)

        button1.setOnClickListener {
            shareData()
        }

        val button2 = view.findViewById<Button>(R.id.button18)

        button2.setOnClickListener {
            shareWhatsApp()
        }

        val referAndEarnLayout = view.findViewById<RelativeLayout>(R.id.referAndEarnLayout)
        referAndEarnLayout.setOnClickListener {
            // Handle the click event here
            // For example, show a Toast message
//            Toast.makeText(requireContext(), "Blank space clicked", Toast.LENGTH_SHORT).show()
        }

        return view
    }



    private fun shareData() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this amazing app: https://your-app-link.com")
// app link provided in the last!!
        val chooserIntent = Intent.createChooser(shareIntent, "Share via")
        if (shareIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(chooserIntent)
        }
    }

//    private fun shareWhatsApp() {
//        val shareIntent = Intent(Intent.ACTION_SEND)
//        shareIntent.type = "text/plain"
//        shareIntent.setPackage("com.whatsapp") // Specify the package name for WhatsApp
//        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this amazing app: https://your-app-link.com")
//
//        if (shareIntent.resolveActivity(requireActivity().packageManager) != null) {
//            startActivity(shareIntent)
//        } else {
//            // WhatsApp is not installed or not supported
//            // You can handle this case accordingly, for example, show a message to the user
//            Toast.makeText(requireContext(), "WhatsApp is not installed or not supported", Toast.LENGTH_SHORT).show()
//        }
//    }

    private fun shareWhatsApp() {
        val message = "Check out this amazing app: https://your-app-link.com"

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.setPackage("com.whatsapp")

        shareIntent.putExtra(Intent.EXTRA_TEXT, message)

        try {
            startActivity(shareIntent)
        } catch (e: ActivityNotFoundException) {
            // WhatsApp is not installed or not supported
            // You can handle this case accordingly, for example, show a message to the user
            Toast.makeText(requireContext(), "WhatsApp is not installed or not supported", Toast.LENGTH_SHORT).show()
        }
    }

    fun onBlankSpaceClicked(view: View) {
        Toast.makeText(requireContext(), "Blank space clicked", Toast.LENGTH_SHORT).show()
    }


}