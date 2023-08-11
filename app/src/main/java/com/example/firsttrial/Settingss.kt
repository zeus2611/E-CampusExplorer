package com.example.firsttrial

import android.app.AlertDialog
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import com.example.firsttrial.Fragment.FourthhFragment
import java.util.Locale


class Settingss : Fragment() {

    private lateinit var context: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settingss, container, false)
        val button = view.findViewById<Button>(R.id.button12)

        button.setOnClickListener {
            val languages = arrayOf("Hindi", "English")

            val langSelectorBuilder= AlertDialog.Builder(context)
            langSelectorBuilder.setTitle("Choose Language:")
            langSelectorBuilder.setSingleChoiceItems(languages,-1)
            { dialog, selection ->
                when(selection){
                    0 -> {
                        setLocale("hi")
                    }
                    1 -> {
                        setLocale("en")
                    }
                }
                recreate()
                dialog.dismiss()
            }
            langSelectorBuilder.create().show()
        }

        val button1 = view.findViewById<ImageView>(R.id.imageView99)
        button1.setOnClickListener {
            val newFragment = FourthhFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransition = fragmentManager.beginTransaction()
            fragmentTransition.replace(R.id.f4, newFragment)
            fragmentTransition.addToBackStack(null)
            fragmentTransition.commit()

        }

        val referAndEarnLayout = view.findViewById<RelativeLayout>(R.id.s1)
        referAndEarnLayout.setOnClickListener {
            // Handle the click event here
            // For example, show a Toast message
//            Toast.makeText(requireContext(), "Blank space clicked", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    private fun recreate() {
        requireActivity().recreate()
    }


}