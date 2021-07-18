package com.samyak.cowin_tracker.presentation.ui.pincode_list

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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

    private val firebaseDatabase: FirebaseDatabase = Firebase.database
    private val usersDatabaseRef = firebaseDatabase.reference.child("users_2_0")

    lateinit var userPincodesDatabaseListener: ValueEventListener

    var pincodes: MutableState<MutableList<Pincode>> = mutableStateOf(mutableListOf())

    fun setUserId(userId: String) {
        this.userId.value = userId
        if (userId != "") {
            initDatabaseListener()
        } else {
            if (this::userPincodesDatabaseListener.isInitialized) {
                detachDatabaseListener()
            }
        }
    }

    fun initDatabaseListener() {
        val userPincodesDatabaseListener = usersDatabaseRef
            .child(userId.value)
            .child("Pincodes").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d(TAG, "onDataChange: ${snapshot.value}")
                    if (snapshot.value != null) {
                        pincodes.value = pincodeDtoMapper
                            .mapToDomainModel(snapshot.value as HashMap<String, HashMap<String, String>>)
                            .pincodes
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        usersDatabaseRef
            .child(userId.value)
            .child("Pincodes").addValueEventListener(userPincodesDatabaseListener)
    }

    fun detachDatabaseListener() {
        usersDatabaseRef
            .child(userId.value)
            .child("Pincodes").removeEventListener(userPincodesDatabaseListener)
    }

}