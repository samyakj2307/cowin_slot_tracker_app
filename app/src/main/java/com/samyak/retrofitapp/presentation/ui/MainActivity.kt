package com.samyak.retrofitapp.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.samyak.retrofitapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val TAG: String = "SamyakMainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}


//    private fun getData() {
//        Log.d(TAG, "Hello")
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val service = retrofit.create(Service::class.java)
//        val call = service.getSlotData()
//        call.enqueue(
//            object : Callback<Json> {
//                override fun onResponse(call: Call<Json>, response: Response<Json>) {
//                    Log.d(TAG, response.code().toString())
//                    response.body()?.let { Log.d(TAG, it.centers[0].state_name) }
//                    response.body()?.let { Log.d(TAG, it.centers[0].district_name) }
//                    response.body()?.let { Log.d(TAG, it.centers[0].fee_type) }
//                    response.body()?.let { Log.d(TAG, it.centers[0].block_name) }
//                    response.body()
//                        ?.let { Log.d(TAG, it.centers[0].vaccine_fees[0].fee.toString()) }
//                }
//
//                override fun onFailure(call: Call<Json>, t: Throwable) {
//                    Log.d(TAG, t.toString())
//                }
//
//            }
//        )
//    }