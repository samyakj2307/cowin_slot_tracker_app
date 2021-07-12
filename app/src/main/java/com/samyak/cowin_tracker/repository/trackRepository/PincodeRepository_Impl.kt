package com.samyak.cowin_tracker.repository.trackRepository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.samyak.cowin_tracker.domain.model.UserPincodes

class PincodeRepository_Impl : PincodeRepository {
    private lateinit var database: DatabaseReference

    override suspend fun getPincodes(userId: String): UserPincodes {
        database = Firebase.database.reference
        var pincodes: UserPincodes
        database.child("users")
            .child(userId)
            .child("Pincodes")
            .get().addOnSuccessListener {
                pincodes = it.value as UserPincodes
            }

        return UserPincodes(listOf())

    }
}