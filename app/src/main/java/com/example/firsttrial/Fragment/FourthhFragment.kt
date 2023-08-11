package com.example.firsttrial.Fragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.firsttrial.R
import com.example.firsttrial.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.firsttrial.Offers
import com.example.firsttrial.Ride_history
import com.example.firsttrial.Settingss
import com.example.firsttrial.referAndEarn
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class FourthhFragment : Fragment() {
    private lateinit var mDatabase: DatabaseReference
    private lateinit var userId: String
    private lateinit var nameTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fourthh, container, false)

        nameTextView = view.findViewById(R.id.textView)
        val editButton = view.findViewById<ImageView>(R.id.editButton)
        editButton.setOnClickListener {
            showEditDialog(nameTextView.text.toString())
        }
        val button = view.findViewById<Button>(R.id.button16)

        button.setOnClickListener {
            val newFragment = Settingss()
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransition = fragmentManager.beginTransaction()
            fragmentTransition.replace(R.id.f4, newFragment)
            fragmentTransition.addToBackStack(null)
            fragmentTransition.commit()

        }

        val button1 = view.findViewById<Button>(R.id.button15)
        button1.setOnClickListener {
            val newFragment = referAndEarn()
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransition = fragmentManager.beginTransaction()
            fragmentTransition.replace(R.id.f4, newFragment)
            fragmentTransition.addToBackStack(null)
            fragmentTransition.commit()

        }
        val button2 = view.findViewById<Button>(R.id.button14)
        button2.setOnClickListener {
            val newFragment = Ride_history()
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransition = fragmentManager.beginTransaction()
            fragmentTransition.replace(R.id.f4, newFragment)
            fragmentTransition.addToBackStack(null)
            fragmentTransition.commit()

        }
        val button3 = view.findViewById<Button>(R.id.button13)
        button3.setOnClickListener {
            val newFragment = Offers()
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransition = fragmentManager.beginTransaction()
            fragmentTransition.replace(R.id.f4, newFragment)
            fragmentTransition.addToBackStack(null)
            fragmentTransition.commit()

        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mDatabase = FirebaseDatabase.getInstance().reference
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: "166754246093-60pofju6qqcktaono53bpgg1klse9mrc.apps.googleusercontent.com"

        mDatabase.child("Users").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

                // Retrieve the user account using GoogleSignIn
                val googleSignInAccount: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(requireContext())

                // Update the UI with the user's profile information
                if (googleSignInAccount != null) {
                    val emailTextView = view.findViewById<TextView>(R.id.textView2)
                    val profileImageView = view.findViewById<ImageView>(R.id.imageView)

                    nameTextView.text = googleSignInAccount.displayName
                    emailTextView.text = googleSignInAccount.email

                    // Load and display the profile picture using Glide library
                    Glide.with(requireContext())
                        .load(googleSignInAccount.photoUrl)
                        .apply(RequestOptions.bitmapTransform(CircleCrop()))
                        .into(profileImageView)
                } else {
                    Log.i("google", "Google account not found")

                    val emailTextView = view.findViewById<TextView>(R.id.textView2)
                    val profileImageView = view.findViewById<ImageView>(R.id.imageView)

                    // Set default values when the user is not signed in with a Google ID
                    nameTextView.text = ""
                    emailTextView.text = ""
                    profileImageView.setImageResource(R.drawable.ellipse_5__1_) // Set default image resource
                }

                if (user != null) {
                    // Update UI with user data
                    val emailTextView = view.findViewById<TextView>(R.id.textView2)

                    nameTextView.text = user.name
                    emailTextView.text = user.email

                    Log.i("firebase", "Got value: $user")
                } else {
                    Log.i("firebase", "User not found")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("firebase", "Error getting data", databaseError.toException())
            }
        })
    }

    private fun updateName(newName: String) {
        mDatabase.child("Users").child(userId).child("name").setValue(newName)
            .addOnSuccessListener {
                Log.i("firebase", "Name updated successfully")
            }
            .addOnFailureListener { e ->
                Log.e("firebase", "Error updating name", e)
            }
    }

    private fun disableEditMode() {
        nameTextView.isEnabled = false

        // Hide the keyboard
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(nameTextView.windowToken, 0)
    }

    private fun showEditDialog(currentName: String) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_dialog_edit_name, null)
        val editTextName = dialogView.findViewById<EditText>(R.id.editTextName)
        editTextName.setText(currentName)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Edit Name")
            .setView(dialogView)
            .setPositiveButton("Save") { dialog, _ ->
                val newName = editTextName.text.toString().trim()
                updateName(newName)
                nameTextView.text = newName
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }
}