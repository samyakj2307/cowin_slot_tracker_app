package com.samyak.cowin_tracker.repository.trackRepository

import com.samyak.cowin_tracker.domain.model.UserPincodes

interface PincodeRepository {

    suspend fun getPincodes(userId:String): UserPincodes
}