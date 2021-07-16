package com.samyak.cowin_tracker.repository.trackRepository

//TODO Delete this
//
//import com.google.firebase.database.DatabaseReference
//
//class PincodeRepository_Impl : PincodeRepository {
//    private lateinit var databaseRef: DatabaseReference
//
////    override suspend fun getPincodes(userId: String): UserPincodes {
////
////        databaseRef = FirebaseDatabase.getInstance().reference.child("users")
////            .child("zfBEUMzoEPcV6gaZSQxcTO4hOsj2").child("Pincodes")
////
////
////        lateinit var pincodes: UserPincodes
////
////
////        CoroutineScope(IO).launch {
////            databaseRef
////                .get().addOnSuccessListener {
////                    pincodes = it.getValue(UserPincodes::class.java)!!
////                    Log.d(TAG, "getPincodes: Hello Whatcha Doing")
////                }
////        }
////
////        return pincodes
////}
//}