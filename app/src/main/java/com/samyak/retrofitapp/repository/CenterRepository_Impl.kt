package com.samyak.retrofitapp.repository

import com.samyak.retrofitapp.domain.model.Center
import com.samyak.retrofitapp.network.CenterService
import com.samyak.retrofitapp.network.model.CenterDtoMapper

class CenterRepository_Impl(
    private val centerService: CenterService,
    private val mapper: CenterDtoMapper
) : CenterRepository {
    override suspend fun getCenters(pincode: String, date: String): List<Center> {
        return mapper.toDomainList(centerService.getCenters(pincode, date).centers)
    }
}