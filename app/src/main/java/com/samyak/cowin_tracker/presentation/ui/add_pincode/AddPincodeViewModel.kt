package com.samyak.cowin_tracker.presentation.ui.add_pincode

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.samyak.cowin_tracker.TAG
import com.samyak.cowin_tracker.domain.model.Pincode
import com.samyak.cowin_tracker.listFeeType
import com.samyak.cowin_tracker.listSlotType
import com.samyak.cowin_tracker.network.model.PincodeDtoMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Double.parseDouble
import javax.inject.Inject

@HiltViewModel
class AddPincodeViewModel
@Inject constructor(
    private val pincodeDtoMapper: PincodeDtoMapper
) : ViewModel() {

    var pincodeString by mutableStateOf("")
    var isSaveEnabled by mutableStateOf(false)
    var isErrorInPincode by mutableStateOf(false)

    var ageGroupSelected = mutableStateOf("")
    var feeTypeSelected = mutableStateOf("")

    private val firebaseDatabase: FirebaseDatabase = Firebase.database
    private val usersDatabaseRef = firebaseDatabase.reference.child("users_2_0")
    private val trackUserPincodeDatabaseRef =
        firebaseDatabase.reference.child("Track_Pin_Codes_2_0")

    val firebaseMessagingToken = mutableStateOf("")


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

    private fun setFirebaseToken(token: String) {
        firebaseMessagingToken.value = token
    }

    fun addPincode(pincode: Pincode, userId: String) {
        if (firebaseMessagingToken.value == "") {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }
                task.result?.let { setFirebaseToken(it) }

                addToDatabase(pincode = pincode, userId = userId)
            })
        } else {
            addToDatabase(pincode = pincode, userId = userId)
        }
    }

    private fun addToDatabase(pincode: Pincode, userId: String): Unit {
        val slotMapped = pincodeDtoMapper.mapFromDomainPincodeSlotTracking(pincode.slot_tracking)
        usersDatabaseRef
            .child(userId)
            .child("Pincodes")
            .child(pincode.pincode)
            .setValue(pincodeDtoMapper.mapFromDomainPincode(pincode = pincode))
            .addOnSuccessListener {

                if (pincode.fee_type == "Both") {
                    if (pincode.slot_tracking == "All") {

                        addValueToTrackPincodeDatabaseReference(
                            pincode = pincode.pincode,
                            feeType = "Free",
                            slotType = "is_18_plus",
                            userId = userId,
                            firebaseToken = firebaseMessagingToken.value
                        )

                        addValueToTrackPincodeDatabaseReference(
                            pincode = pincode.pincode,
                            feeType = "Free",
                            slotType = "is_45_plus",
                            userId = userId,
                            firebaseToken = firebaseMessagingToken.value
                        )

                        addValueToTrackPincodeDatabaseReference(
                            pincode = pincode.pincode,
                            feeType = "Paid",
                            slotType = "is_18_plus",
                            userId = userId,
                            firebaseToken = firebaseMessagingToken.value
                        )

                        addValueToTrackPincodeDatabaseReference(
                            pincode = pincode.pincode,
                            feeType = "Paid",
                            slotType = "is_45_plus",
                            userId = userId,
                            firebaseToken = firebaseMessagingToken.value
                        )
                    } else {
                        addValueToTrackPincodeDatabaseReference(
                            pincode = pincode.pincode,
                            feeType = "Free",
                            slotType = slotMapped,
                            userId = userId,
                            firebaseToken = firebaseMessagingToken.value
                        )

                        addValueToTrackPincodeDatabaseReference(
                            pincode = pincode.pincode,
                            feeType = "Paid",
                            slotType = slotMapped,
                            userId = userId,
                            firebaseToken = firebaseMessagingToken.value
                        )


                        listFeeType.forEach { feeType ->
                            listSlotType.forEach { slotType ->
                                if (slotType != slotMapped) {
                                    removeValueFromDatabase(
                                        userId = userId,
                                        feeType = feeType,
                                        slotType = slotType,
                                        pincode = pincode.pincode
                                    )
                                }
                            }
                        }
                    }
                } else {
                    if (pincode.slot_tracking == "All") {
                        addValueToTrackPincodeDatabaseReference(
                            pincode = pincode.pincode,
                            feeType = pincode.fee_type,
                            slotType = "is_18_plus",
                            userId = userId,
                            firebaseToken = firebaseMessagingToken.value
                        )

                        addValueToTrackPincodeDatabaseReference(
                            pincode = pincode.pincode,
                            feeType = pincode.fee_type,
                            slotType = "is_45_plus",
                            userId = userId,
                            firebaseToken = firebaseMessagingToken.value
                        )

                        listFeeType.forEach { feeType ->
                            if (feeType != pincode.fee_type) {
                                listSlotType.forEach { slotType ->
                                    removeValueFromDatabase(
                                        userId = userId,
                                        feeType = feeType,
                                        slotType = slotType,
                                        pincode = pincode.pincode
                                    )
                                }
                            }
                        }

                    } else {
                        addValueToTrackPincodeDatabaseReference(
                            pincode = pincode.pincode,
                            feeType = pincode.fee_type,
                            slotType = slotMapped,
                            userId = userId,
                            firebaseToken = firebaseMessagingToken.value
                        )

                        listFeeType.forEach { feeType ->
                            listSlotType.forEach { slotType ->
                                if (feeType != pincode.fee_type) {
                                    removeValueFromDatabase(
                                        userId = userId,
                                        feeType = feeType,
                                        slotType = slotType,
                                        pincode = pincode.pincode
                                    )
                                } else {
                                    if (slotType != slotMapped) {
                                        removeValueFromDatabase(
                                            userId = userId,
                                            feeType = feeType,
                                            slotType = slotType,
                                            pincode = pincode.pincode
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
    }

    private fun removeValueFromDatabase(
        userId: String,
        feeType: String,
        slotType: String,
        pincode: String
    ) {
        trackUserPincodeDatabaseRef
            .child(pincode)
            .child(feeType)
            .child(slotType)
            .child(userId)
            .removeValue()
    }

    private fun addValueToTrackPincodeDatabaseReference(
        userId: String,
        feeType: String,
        slotType: String,
        pincode: String,
        firebaseToken: String
    ) {
        trackUserPincodeDatabaseRef
            .child(pincode)
            .child(feeType)
            .child(slotType)
            .child(userId)
            .setValue(firebaseToken)

    }
}