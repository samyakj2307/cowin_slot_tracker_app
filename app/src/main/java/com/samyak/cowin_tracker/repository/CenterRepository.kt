package com.samyak.cowin_tracker.repository

import com.samyak.cowin_tracker.domain.model.Center

interface CenterRepository {

    suspend fun getCenters(pincode:String, date:String): List<Center>

}