package com.samyak.cowin_tracker.domain.util

interface DomainMapper <T, DomainModel>{

    fun mapToDomainModel(t: T):DomainModel

    fun mapFromDomainModel(domainModel:DomainModel):T
}