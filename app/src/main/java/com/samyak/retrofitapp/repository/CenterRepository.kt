package com.samyak.retrofitapp.repository

import com.samyak.retrofitapp.domain.model.Center

interface CenterRepository {

    suspend fun getCenters(pincode:String, date:String): List<Center>

}