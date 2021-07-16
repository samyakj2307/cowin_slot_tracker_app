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
import com.samyak.cowin_tracker.presentation.components.navigation.BottomNavigationBar
import com.samyak.cowin_tracker.presentation.ui.pincode_list.PincodeViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var firebaseMessagingAccessToken: String
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }


    private val viewModel: PincodeViewModel by viewModels()

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

            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }

    private fun onSignedInInitialize(userId: String) {
        viewModel.setUserId(userId = userId)
        Log.d(TAG, "onCreate: $userId")
//          TODO set up Firebase Messaging
//        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//            if (!task.isSuccessful) {
//                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
//                return@OnCompleteListener
//            }
//            firebaseMessagingAccessToken = task.result
//        })
    }

    private fun onSignedOutCleanup() {
        viewModel.setUserId("")
        firebaseMessagingAccessToken = ""
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