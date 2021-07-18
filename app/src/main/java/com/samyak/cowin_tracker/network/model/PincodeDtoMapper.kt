package com.samyak.cowin_tracker.network.model

import com.samyak.cowin_tracker.domain.model.Pincode
import com.samyak.cowin_tracker.domain.model.UserPincodes
import com.samyak.cowin_tracker.domain.util.DomainMapper

class PincodeDtoMapper : DomainMapper<HashMap<String, HashMap<String, String>>, UserPincodes> {
    override fun mapToDomainModel(t: HashMap<String, HashMap<String, String>>): UserPincodes {
        val userPincodes = mutableListOf<Pincode>()


        t.forEach { (key, value) ->
            val slot_tracking = value["slot_tracking"]?.let { mapToDomainPincodeSlotTracking(it) }
            val newPincode = value["pincode"]?.let { pincode ->
                value["fee_type"]?.let { fee_type ->
                    slot_tracking?.let { slot_tracking ->
                        Pincode(
                            pincode = pincode,
                            slot_tracking = slot_tracking,
                            fee_type = fee_type
                        )
                    }
                }
            }
            if (newPincode != null) {
                userPincodes.add(newPincode)
            }
        }
        return UserPincodes(userPincodes)
    }

    override fun mapFromDomainModel(domainModel: UserPincodes): HashMap<String, HashMap<String, String>> {
//        return domainModel
//            .pincodes
//            .associate {
//                Pair(
//                    it.pincode,
//                    it.slot_tracking
//                )
//            } as HashMap<String, HashMap<String,String>>
        return HashMap()
    }

    fun mapFromDomainPincode(pincode: Pincode): HashMap<String, String> {
        val map: HashMap<String, String> = HashMap()
        map["pincode"] = pincode.pincode
        map["fee_type"] = pincode.fee_type
        map["slot_tracking"] = mapFromDomainPincodeSlotTracking(pincode.slot_tracking)
        return map
    }

    fun mapToDomainPincodeSlotTracking(slot_tracking: String): String {
        return when (slot_tracking) {
            "is_18_plus" -> "18+"
            "is_45_plus" -> "45+"
            else -> "All"
        }
    }

    fun mapFromDomainPincodeSlotTracking(slot_tracking: String): String {
        return when (slot_tracking) {
            "18+" -> {
                "is_18_plus"
            }
            "45+" -> {
                "is_45_plus"
            }
            else -> {
                "is_all"
            }
        }
    }

}