package com.samyak.retrofitapp.network.model

import android.util.Log
import com.samyak.retrofitapp.TAG
import com.samyak.retrofitapp.domain.model.Center
import com.samyak.retrofitapp.domain.model.Session
import com.samyak.retrofitapp.domain.model.VaccineFee
import com.samyak.retrofitapp.domain.util.DomainMapper

class CenterDtoMapper : DomainMapper<CenterDto, Center> {

    override fun mapToDomainModel(t: CenterDto): Center {
        return Center(
            center_id = t.center_id,
            name = t.name,
            address = t.address,
            state_name = t.state_name,
            district_name = t.district_name,
            block_name = t.block_name,
            pincode = t.pincode,
            latitude = t.latitude,
            longitude = t.longitude,
            from = t.from,
            to = t.to,
            fee_type = t.fee_type,
            sessions = toDomainSessionList(t.sessions ?: listOf()),
            vaccine_fees = toDomainVaccineList(t.vaccine_fees ?: listOf())
        )
    }

    //Not Used for Posting Data
    override fun mapFromDomainModel(domainModel: Center): CenterDto {
        return CenterDto(
            center_id = domainModel.center_id,
            name = domainModel.name,
            address = domainModel.address,
            state_name = domainModel.state_name,
            district_name = domainModel.district_name,
            block_name = domainModel.block_name,
            pincode = domainModel.pincode,
            latitude = domainModel.latitude,
            longitude = domainModel.longitude,
            from = domainModel.from,
            to = domainModel.to,
            fee_type = domainModel.fee_type,
        )
    }

    private fun mapToDomainSessionModel(t: SessionDto): Session {
        return Session(
            session_id = t.session_id,
            date = t.date,
            available_capacity = t.available_capacity,
            min_age_limit = t.min_age_limit,
            max_age_limit = t.max_age_limit,
            vaccine = t.vaccine,
            slots = t.slots,
            available_capacity_dose1 = t.available_capacity_dose1,
            available_capacity_dose2 = t.available_capacity_dose2,
        )
    }

    private fun mapToDomainVaccineModel(t: VaccineFeeDto): VaccineFee {
        return VaccineFee(
            vaccine = t.vaccine,
            fee = t.fee,
        )
    }


    private fun toDomainVaccineList(initial: List<VaccineFeeDto>): List<VaccineFee> {
        return initial.map { mapToDomainVaccineModel(it) }
    }


    private fun toDomainSessionList(initial: List<SessionDto>): List<Session> {
        return initial.map { mapToDomainSessionModel(it) }
    }


    fun toDomainList(initial: List<CenterDto>): List<Center> {
        return initial.map { mapToDomainModel(it) }
    }


    //Not Used for Posting Data
    fun fromDomainList(initial: List<Center>): List<CenterDto> {
        return initial.map { mapFromDomainModel(it) }
    }

}