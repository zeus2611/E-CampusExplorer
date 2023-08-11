package com.example.firsttrial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.firsttrial.Fragment.FirsttFragment
import com.example.firsttrial.Fragment.FourthhFragment
import com.example.firsttrial.Fragment.SeconddFragment
import com.example.firsttrial.Fragment.ThirddFragment
import com.example.firsttrial.databinding.FragmentNavtryBinding

class navtry : Fragment() {

    private lateinit var binding: FragmentNavtryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNavtryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val firstttFragment = FirsttFragment()
        val secondddFragment = SeconddFragment()
        val thirdddFragment = ThirddFragment()
        val fourthhhFragment = FourthhFragment()

        setCurrentFragment(firstttFragment)

        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.firsttFragment -> setCurrentFragment(firstttFragment)
                R.id.seconddFragment -> setCurrentFragment(secondddFragment)
                R.id.thirddFragment -> setCurrentFragment(thirdddFragment)
                R.id.fourthhFragment -> setCurrentFragment(fourthhhFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, fragment)
            commit()
            addToBackStack(null)
        }
    }
}
