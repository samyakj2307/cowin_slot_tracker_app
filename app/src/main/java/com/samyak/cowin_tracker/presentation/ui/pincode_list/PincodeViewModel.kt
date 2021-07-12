package com.samyak.cowin_tracker.presentation.ui.pincode_list

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PincodeViewModel
@Inject constructor(

) : ViewModel() {
    val userId = mutableStateOf("")

    val firebaseAuth: FirebaseAuth = Firebase.auth
    val firebaseDatabase: FirebaseDatabase = Firebase.database

    val pincodes = mutableListOf(mutableMapOf<String, String>())


    fun setUserId(userId: String) {
        this.userId.value = userId
    }

}