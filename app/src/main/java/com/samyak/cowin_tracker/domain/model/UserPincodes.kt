package com.samyak.cowin_tracker.domain.model

data class UserPincodes(
    val pincodes: MutableList<Pincode>
)

data class Pincode(
    val pincode: String,
    val slot_tracking: String,
    val fee_type: String
)
