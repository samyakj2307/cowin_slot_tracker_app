package com.samyak.retrofitapp.network.responses

import com.google.gson.annotations.SerializedName
import com.samyak.retrofitapp.network.model.CenterDto

data class CenterSearchResponse(

    @SerializedName("centers")
    var centers: List<CenterDto>

)