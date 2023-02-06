package com.acm431.complaintmanagement.model

import com.google.firebase.Timestamp

data class Complaint(
    var complaintID: String = "",
    val content : String= "",
    var imagePath: String= "",
    val location : String= "",
    val date : String = Timestamp.now().toDate().toString(),
    val status: String= "",
    val urgency: String= "",
    val userName: String = "",
    val solved : Int = 0,
    val latitude: Double? = 0.0,
    val longitude : Double? = 0.0,
    )
