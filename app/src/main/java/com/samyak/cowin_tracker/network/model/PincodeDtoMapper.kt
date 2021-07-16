package com.samyak.cowin_tracker.network.model

import com.samyak.cowin_tracker.domain.model.Pincode
import com.samyak.cowin_tracker.domain.model.UserPincodes
import com.samyak.cowin_tracker.domain.util.DomainMapper

class PincodeDtoMapper : DomainMapper<HashMap<String, String>, UserPincodes> {
    override fun mapToDomainModel(t: HashMap<String, String>): UserPincodes {
        val userPincodes = mutableListOf<Pincode>()
        t.forEach { (key, value) ->
            var slot_tracking = ""
            if (value == "is_18_plus") {
                slot_tracking = "18+"
            } else if (value == "is_45_plus") {
                slot_tracking = "45+"
            } else {
                slot_tracking = "All"
            }
            userPincodes.add(Pincode(pincode = key, slot_tracking = slot_tracking))
        }
        return UserPincodes(userPincodes)
    }

    override fun mapFromDomainModel(domainModel: UserPincodes): HashMap<String, String> {
        return domainModel
            .pincodes
            .associate {
                Pair(
                    it.pincode,
                    it.slot_tracking
                )
            } as HashMap<String, String>
    }

    fun mapFromDomainPincode(pincode: Pincode): HashMap<String, String> {
        val map: HashMap<String, String> = HashMap()
        map[pincode.pincode] = pincode.slot_tracking
        return map
    }

}