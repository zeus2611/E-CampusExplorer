package com.example.firsttrial

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.firsttrial.databinding.FragmentLoginsignuppageBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class Loginsignuppage : Fragment() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 123 // Constant for sign-in request
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private val binding by lazy {
        FragmentLoginsignuppageBinding.inflate(layoutInflater)
    }
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
        }
    }
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("com.example.firsttrial.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = binding.root
        val button = view.findViewById<Button>(R.id.button4)
        val btn = view.findViewById<Button>(R.id.button3)

        auth = Firebase.auth
        database = FirebaseDatabase.getInstance().reference.child("Users")

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("166754246093-60pofju6qqcktaono53bpgg1klse9mrc.apps.googleusercontent.com") // Replace with the resource ID of your web client ID
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        btn.setOnClickListener {
            signInWithGoogle()
        }

        button.setOnClickListener {
            val name = binding.name1.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all the details", Toast.LENGTH_SHORT).show()
            } else {
                // Check if user already exists with the same email
                val query = database.orderByChild("email").equalTo(email)
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // User already exists with the same email
                            Toast.makeText(requireContext(), "User with this email already exists", Toast.LENGTH_SHORT).show()
                        } else {
                            auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener { createTask ->
                                    if (createTask.isSuccessful) {
                                        val userId = auth.currentUser?.uid ?: "default_user_id"
                                        val user = User(name, email, password)
                                        database.child(userId).setValue(user)
                                            .addOnSuccessListener {
                                                // Data saved to the database successfully
                                                // Navigate to the next page
                                                saveLoginStatus(true)
                                                findNavController().navigate(R.id.enablelocation)
                                            }
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            createTask.exception?.localizedMessage,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(
                            requireContext(),
                            "Database error: ${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        }

        // Check if the user is already logged in
        if (isLoggedIn()) {
            findNavController().navigate(R.id.navtry)
        }

        return view
    }

    private fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("IS_LOGGED_IN", false)
    }

    private fun saveLoginStatus(isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("IS_LOGGED_IN", isLoggedIn)
        editor.apply()
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account?.idToken

            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener { signInTask ->
                    if (signInTask.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        if (userId != null) {
                            val user = User(
                                name = account?.displayName ?: "Textview",
                                email = account?.email ?: "Textview2",
                                password = "" // You may leave it empty or provide a placeholder value
                            )

                            database.child(userId).setValue(user)
                                .addOnSuccessListener {
                                    // Data saved to the database successfully
                                    saveLoginStatus(true)
                                    // Navigate to the next page
                                    findNavController().navigate(R.id.enablelocation)
                                }
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            signInTask.exception?.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } catch (e: ApiException) {
            Toast.makeText(requireContext(), "Sign-in failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }
}
