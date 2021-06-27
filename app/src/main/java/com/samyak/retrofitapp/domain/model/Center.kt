package com.samyak.retrofitapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Center(
    val center_id: Int? = 0,
    val name: String? = null,
    val address: String? = null,
    val state_name: String? = null,
    val district_name: String? = null,
    val block_name: String? = null,
    val pincode: Long? = 0,
    val latitude: Double? = 0.0,
    val longitude: Double? = 0.0,
    val from: String? = null,
    val to: String? = null,
    val fee_type: String? = null,
    val sessions: List<Session>? = listOf(),
    val vaccine_fees: List<VaccineFee>? = listOf(),
) : Parcelable

@Parcelize
data class VaccineFee(
    val vaccine: String? = null,
    val fee: Int? = 0,
) : Parcelable


@Parcelize
data class Session(
    val session_id: String? = null,
    val date: String? = null,
    val available_capacity: Long? = 0,
    val min_age_limit: Int? = 0,
    val vaccine: String? = null,
    val slots: List<String>? = listOf(),
    val available_capacity_dose1: Long? = 0,
    val available_capacity_dose2: Long? = 0,
) : Parcelable