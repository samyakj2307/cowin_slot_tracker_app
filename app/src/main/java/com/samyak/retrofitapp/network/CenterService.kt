package com.samyak.retrofitapp.network

import com.samyak.retrofitapp.network.responses.CenterSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CenterService {

    @GET("calendarByPin")
    suspend fun getCenters(
        @Query("pincode") pincode: String,
        @Query("date") date: String,
    ): CenterSearchResponse

}