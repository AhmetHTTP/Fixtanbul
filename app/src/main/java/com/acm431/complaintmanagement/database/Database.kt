package com.acm431.complaintmanagement.database

import com.acm431.complaintmanagement.view.complaintviews.tabfragments.AdminActiveFragment
import com.google.firebase.firestore.FirebaseFirestore

class Database {

    private val db = FirebaseFirestore.getInstance()

    fun editComplaintStatus(fragment:AdminActiveFragment, complaintId : String){

        db.collection("allComplaints")
            .document(complaintId)
            .update("solved",1)
            .addOnSuccessListener {
                db.collection("allComplaints")
                    .document(complaintId)
                    .update("status","Solved")
                fragment.solvedSuccess()
            }
            .addOnFailureListener {
                fragment.hideProgressBar()
            }




    }

}