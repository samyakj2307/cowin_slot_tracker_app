package com.samyak.cowin_tracker.presentation.ui

import android.os.Bundle
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
import com.samyak.cowin_tracker.presentation.components.navigation.BottomNavigationBar
import com.samyak.cowin_tracker.presentation.ui.center_list.CenterListViewModel
import com.samyak.cowin_tracker.presentation.ui.pincode_list.PincodeViewModel
import com.samyak.cowin_tracker.presentation.util.ConnectionLiveData
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Integer.parseInt


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    lateinit var connectionLiveData: ConnectionLiveData

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }


    private val pincodeViewModel: PincodeViewModel by viewModels()

    private val centerListViewModel: CenterListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        connectionLiveData = ConnectionLiveData(this)


        setContentView(R.layout.activity_main)
        firebaseAuth = FirebaseAuth.getInstance()



        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser != null) {

                onSignedInInitialize(firebaseUser.uid)
            } else {
                onSignedOutCleanup()
                createSignInIntent()
            }
        }

        connectionLiveData.observe(this, { isNetworkAvailable ->
            centerListViewModel.isNetworkAvailable.value = isNetworkAvailable
        })

        val bottomNavigationView = findViewById<ComposeView>(R.id.bottom_navigation_view)

        bottomNavigationView.setContent {
            BottomNavigationBar(navController = findNavController(R.id.main_nav_host_fragment))
        }

    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onResume() {
        super.onResume()
        if (intent.extras != null) {
            val pincode: String = intent.extras!!.get("pincode") as String
            val center_id = intent.extras!!.get("center_id") as String


            centerListViewModel.query.value = pincode
            if (centerListViewModel.isNetworkAvailable.value) {
                centerListViewModel.newSearchForNotification(pincode, parseInt(center_id))
            }

            centerListViewModel.isNotificationSearchDone.observe(this, { value ->
                if (value == true) {
                    findNavController(R.id.main_nav_host_fragment).navigate(R.id.notificationIntent)
                }
            }
            )
        }
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
    }

    private fun onSignedOutCleanup() {
        pincodeViewModel.setUserId("")
        pincodeViewModel.pincodes.value = mutableListOf()
        centerListViewModel.centers.value = listOf()
    }

//    private fun signOut() {
//        this.let {
//            AuthUI.getInstance()
//                .signOut(it)
//                .addOnCompleteListener {
//                    onSignedOutCleanup()
//                }
//        }
//    }

}