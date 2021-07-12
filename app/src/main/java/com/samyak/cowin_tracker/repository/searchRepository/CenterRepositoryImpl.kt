package com.samyak.cowin_tracker.repository.searchRepository

import com.samyak.cowin_tracker.domain.model.Center
import com.samyak.cowin_tracker.network.CenterService
import com.samyak.cowin_tracker.network.model.CenterDtoMapper
import com.samyak.cowin_tracker.repository.searchRepository.CenterRepository

class CenterRepositoryImpl(
    private val centerService: CenterService,
    private val mapper: CenterDtoMapper
) : CenterRepository {
    override suspend fun getCenters(pincode: String, date: String): List<Center> {
        return mapper.toDomainList(centerService.getCenters(pincode, date).centers)
    }
}