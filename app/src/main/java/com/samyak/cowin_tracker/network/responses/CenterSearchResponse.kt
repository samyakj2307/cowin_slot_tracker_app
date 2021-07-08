package com.samyak.cowin_tracker.network.responses

import com.google.gson.annotations.SerializedName
import com.samyak.cowin_tracker.network.model.CenterDto

data class CenterSearchResponse(

    @SerializedName("centers")
    var centers: List<CenterDto>

)