package com.samyak.cowin_tracker.presentation.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.samyak.cowin_tracker.R
import com.samyak.cowin_tracker.TAG
import com.samyak.cowin_tracker.domain.model.Center
import com.samyak.cowin_tracker.presentation.components.navigation.BottomNavigationBar
import com.samyak.cowin_tracker.presentation.ui.center_list.CenterListViewModel
import com.samyak.cowin_tracker.presentation.ui.pincode_list.PincodeViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }


    private val pincodeViewModel: PincodeViewModel by viewModels()

    private val centerListViewModel: CenterListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseAuth = FirebaseAuth.getInstance()

//        signOut()

        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser != null) {
                Log.d(TAG, "onCreate: ${firebaseUser.uid}")

                onSignedInInitialize(firebaseUser.uid)
            } else {
                Log.d(TAG, "onCreate: ")
                onSignedOutCleanup()
                createSignInIntent()
            }
        }

        val bottomNavigationView = findViewById<ComposeView>(R.id.bottom_navigation_view)

        bottomNavigationView.setContent {
            BottomNavigationBar(navController = findNavController(R.id.main_nav_host_fragment))
        }

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onResume() {
        super.onResume()
//        if (intent.extras != null) {
//            Log.d(TAG, "onResume: ${intent.extras!!.get("available_capacity")}")
//            Log.d(TAG, "onResume: ${intent.extras!!.get("slot_type")}")
//            Log.d(TAG, "onResume: ${intent.extras!!.get("date")}")
//            Log.d(TAG, "onResume: ${intent.extras!!.get("session_id")}")
//            Log.d(TAG, "onResume: ${intent.extras!!.get("center_id")}")
//
//            val pincode: String = intent.extras!!.get("pincode") as String
//            centerListViewModel.newSearch(pincode)
//            Log.d(TAG, "onResume: ${centerListViewModel.centers}")
//            Log.d(TAG, "onResume: ${centerListViewModel.centers.value.indexOf(Center(center_id = intent.extras!!.get("center_id") as Int?))}")
//            findNavController(R.id.notificationIntent)
//        }
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(authStateListener)
    }

    private fun createSignInIntent() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().setDefaultCountryIso("IN").build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
        )

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setIsSmartLockEnabled(false)
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            user?.uid?.let { onSignedInInitialize(it) }
        } else {
            Toast.makeText(this, "Signin Failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onSignedInInitialize(userId: String) {
        pincodeViewModel.setUserId(userId = userId)
        Log.d(TAG, "onCreate: $userId")
    }

    private fun onSignedOutCleanup() {
        pincodeViewModel.setUserId("")
    }

    private fun signOut() {
        this.let {
            AuthUI.getInstance()
                .signOut(it)
                .addOnCompleteListener {
                    onSignedOutCleanup()
                }
        }
    }

}