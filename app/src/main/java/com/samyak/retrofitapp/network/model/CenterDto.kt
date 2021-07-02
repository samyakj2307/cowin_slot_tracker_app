package com.samyak.retrofitapp.network.model

import com.google.gson.annotations.SerializedName

data class CenterDto(

    @SerializedName("center_id")
    val center_id: Int? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("address")
    val address: String? = null,

    @SerializedName("state_name")
    val state_name: String? = null,

    @SerializedName("district_name")
    val district_name: String? = null,

    @SerializedName("block_name")
    val block_name: String? = null,

    @SerializedName("pincode")
    val pincode: Long? = 0,

    @SerializedName("lat")
    val latitude: Double? = 0.0,


    @SerializedName("long")
    val longitude: Double? = 0.0,


    @SerializedName("from")
    val from: String? = null,


    @SerializedName("to")
    val to: String? = null,


    @SerializedName("fee_type")
    val fee_type: String? = null,

    @SerializedName("sessions")
    val sessions: List<SessionDto>? = listOf(),


    @SerializedName("vaccine_fees")
    val vaccine_fees: List<VaccineFeeDto>? = listOf(),
)

class VaccineFeeDto(
    @SerializedName("vaccine")
    val vaccine: String? = null,

    @SerializedName("fee")
    val fee: Int? = 0,
)

class SessionDto(

    @SerializedName("session_id")
    val session_id: String? = null,

    @SerializedName("date")
    val date: String? = null,

    @SerializedName("available_capacity")
    val available_capacity: Long? = 0,

    @SerializedName("min_age_limit")
    val min_age_limit: Int? = 0,

    @SerializedName("max_age_limit")
    val max_age_limit: Int? = 0,

    @SerializedName("vaccine")
    val vaccine: String? = null,

    @SerializedName("slots")
    val slots: List<String>? = listOf(),

    @SerializedName("available_capacity_dose1")
    val available_capacity_dose1: Long? = 0,

    @SerializedName("available_capacity_dose2")
    val available_capacity_dose2: Long? = 0,
)