package com.samyak.cowin_tracker.presentation.ui.add_pincode

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.samyak.cowin_tracker.domain.model.Pincode
import com.samyak.cowin_tracker.network.model.PincodeDtoMapper
import java.lang.Double.parseDouble

class AddPincodeViewModel : ViewModel() {

    var pincodeString by mutableStateOf("")
    var isSaveEnabled by mutableStateOf(false)
    var isErrorInPincode by mutableStateOf(false)

    var ageGroupSelected = mutableStateOf("")
    var feeTypeSelected = mutableStateOf("")

    private val firebaseDatabase: FirebaseDatabase = Firebase.database
    private val usersDatabaseRef = firebaseDatabase.reference.child("users")


    fun onChangePincodeString(pincode: String): Unit {
        this.pincodeString = pincode

        var numeric = true

        try {
            val num = parseDouble(pincode)
        } catch (e: NumberFormatException) {
            numeric = false
        }

        this.isSaveEnabled = pincode.length == 6 && numeric
    }

    fun addPincode(pincode: Pincode, userId: String) {
        usersDatabaseRef.child(userId)
            .child("Pincodes").child(pincode.pincode).setValue(pincode.slot_tracking)
//            .setValue(PincodeDtoMapper().mapFromDomainPincode(pincode = pincode))
    }

}