package com.samyak.cowin_tracker.presentation.ui.pincode_list

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.samyak.cowin_tracker.TAG
import com.samyak.cowin_tracker.repository.trackRepository.PincodeRepository_Impl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PincodeListFragment : Fragment() {

    private val viewModel: PincodeViewModel by activityViewModels()

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView: ${viewModel.userId.value}")
        if (viewModel.firebaseAuth.currentUser == null) {
            Log.d(TAG, "onCreateView: Inside if else")
            createSignInIntent()
        } else {
            Log.d(TAG, "onCreateView: ${viewModel.firebaseAuth.currentUser.uid}")
        }

        return ComposeView(requireContext()).apply {
            setContent {
                Scaffold {
                    Column(modifier = Modifier.padding(bottom = it.calculateBottomPadding())) {
                        Text(text = "Hello in Pincode Fragment")
                    }
                }
            }
        }
    }

    private fun createSignInIntent() {
        Log.d(TAG, "createSignInIntent: Reached Inside")
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().setDefaultCountryIso("IN").build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
        )

        Log.d(TAG, "createSignInIntent: Reached Providers")

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
            Log.d(TAG, "onSignInResult: Hello")
            val user = FirebaseAuth.getInstance().currentUser
            user?.uid?.let { viewModel.setUserId(it) }
            CoroutineScope(IO).launch {
                PincodeRepository_Impl().getPincodes(viewModel.userId.value)
            }
        } else {
            Toast.makeText(context, "Signin Failed", Toast.LENGTH_SHORT).show()

            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }

    private fun signOut() {
        context?.let {
            AuthUI.getInstance()
                .signOut(it)
                .addOnCompleteListener {

                }
        }
    }

}