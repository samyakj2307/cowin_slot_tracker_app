package com.samyak.cowin_tracker.presentation.ui.pincode_list

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.samyak.cowin_tracker.TAG
import com.samyak.cowin_tracker.domain.model.Pincode
import com.samyak.cowin_tracker.network.model.PincodeDtoMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PincodeViewModel
@Inject constructor(
    private val pincodeDtoMapper: PincodeDtoMapper
) : ViewModel() {
    val userId = mutableStateOf("")

    val firebaseAuth: FirebaseAuth = Firebase.auth
    private val firebaseDatabase: FirebaseDatabase = Firebase.database
    private val usersDatabaseRef = firebaseDatabase.reference.child("users")
    private val trackUserPincodeDatabaseRef = firebaseDatabase.reference.child("Track Pin Codes")

    var pincodes: MutableState<MutableList<Pincode>> = mutableStateOf(mutableListOf())

    fun setUserId(userId: String) {
        this.userId.value = userId
        getUserPincodes()
    }

    fun getUserPincodes() {
        //TODO change this to $userId
        val userPincodesDatabaseRef = usersDatabaseRef
            .child(userId.value)
            .child("Pincodes")

        userPincodesDatabaseRef
            .get().addOnSuccessListener {
                if (it.value != null) {
                    pincodes.value = PincodeDtoMapper()
                        .mapToDomainModel(it.value as HashMap<String, String>)
                        .pincodes
                    Log.d(TAG, "getUserPincodes:$pincodes")
                }
            }
    }

}