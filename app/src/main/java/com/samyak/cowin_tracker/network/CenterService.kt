package com.samyak.cowin_tracker.network

import com.samyak.cowin_tracker.network.responses.CenterSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CenterService {

    @GET("calendarByPin")
    suspend fun getCenters(
        @Query("pincode") pincode: String,
        @Query("date") date: String,
    ): CenterSearchResponse

}