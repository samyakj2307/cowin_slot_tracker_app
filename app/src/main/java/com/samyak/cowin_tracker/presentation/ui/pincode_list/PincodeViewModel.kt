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
    private val trackUserPincodeDatabaseRef =
        firebaseDatabase.reference.child("Track_Pin_Codes_2_0")

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

    private fun initDatabaseListener() {
        val userPincodesDatabaseListener = usersDatabaseRef
            .child(userId.value)
            .child("Pincodes").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        pincodes.value = pincodeDtoMapper
                            .mapToDomainModel(snapshot.value as HashMap<String, HashMap<String, String>>)
                            .pincodes
                    } else {
                        pincodes.value = mutableListOf()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "onCancelled: $error")
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


    fun removePincode(pincode: Pincode) {
        usersDatabaseRef
            .child(userId.value)
            .child("Pincodes")
            .child(pincode.pincode)
            .removeValue()
            .addOnSuccessListener {

                if (pincode.fee_type == "Both") {
                    if (pincode.slot_tracking == "All") {
                        removeValueFromDatabase(
                            feeType = "Free",
                            slotType = "18+",
                            pincode = pincode.pincode
                        )
                        removeValueFromDatabase(
                            feeType = "Free",
                            slotType = "45+",
                            pincode = pincode.pincode
                        )
                        removeValueFromDatabase(
                            feeType = "Paid",
                            slotType = "18+",
                            pincode = pincode.pincode
                        )
                        removeValueFromDatabase(
                            feeType = "Paid",
                            slotType = "45+",
                            pincode = pincode.pincode
                        )
                    } else {
                        removeValueFromDatabase(
                            feeType = "Free",
                            slotType = pincode.slot_tracking,
                            pincode = pincode.pincode
                        )
                        removeValueFromDatabase(
                            feeType = "Paid",
                            slotType = pincode.slot_tracking,
                            pincode = pincode.pincode
                        )
                    }
                } else {
                    if (pincode.slot_tracking == "All") {

                        removeValueFromDatabase(
                            feeType = pincode.fee_type,
                            slotType = "18+",
                            pincode = pincode.pincode
                        )
                        removeValueFromDatabase(
                            feeType = pincode.fee_type,
                            slotType = "18+",
                            pincode = pincode.pincode
                        )
                    } else {
                        removeValueFromDatabase(
                            feeType = pincode.fee_type,
                            slotType = pincode.slot_tracking,
                            pincode = pincode.pincode
                        )
                    }
                }
            }
    }

    private fun removeValueFromDatabase(
        feeType: String,
        slotType: String,
        pincode: String
    ) {
        trackUserPincodeDatabaseRef
            .child(pincode)
            .child(feeType)
            .child(pincodeDtoMapper.mapFromDomainPincodeSlotTracking(slot_tracking = slotType))
            .child(userId.value)
            .removeValue()
    }

}