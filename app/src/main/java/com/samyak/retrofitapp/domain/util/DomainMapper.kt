package com.samyak.retrofitapp.domain.util

interface DomainMapper <T, DomainModel>{

    fun mapToDomainModel(t: T):DomainModel

    fun mapFromDomainModel(domainModel:DomainModel):T
}