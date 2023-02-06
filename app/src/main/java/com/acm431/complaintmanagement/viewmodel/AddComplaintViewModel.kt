package com.acm431.complaintmanagement.viewmodel

import android.net.Uri
import android.provider.Settings.Global
import androidx.lifecycle.ViewModel
import com.acm431.complaintmanagement.database.GlobalValues
import com.acm431.complaintmanagement.model.Complaint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class AddComplaintViewModel : ViewModel() {

    private val database = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val collectionRef = database.collection("users")
        .document(auth.currentUser!!.email.toString())
        .collection("complaints")
    val uuid = UUID.randomUUID()
    private val gorselIsmi="${uuid}.jpg"
    private val storage = FirebaseStorage.getInstance()

    fun save(data: Complaint) {

        val newRef = collectionRef.document()
        data.complaintID = newRef.id
        newRef.set(data)
            .addOnFailureListener { exception ->
                throw Exception(exception.localizedMessage)
            }
        database.collection("allComplaints").document(data.complaintID).set(data)
    }

    fun saveImageToStorage (uri : Uri, data : Complaint) {
        storage.reference.child("images").child(gorselIsmi).putFile(uri).addOnSuccessListener {
            storage.reference.child("images").child(gorselIsmi).downloadUrl.addOnSuccessListener {
                data.imagePath = it.toString()
                save(data)
            }
        }
    }
}