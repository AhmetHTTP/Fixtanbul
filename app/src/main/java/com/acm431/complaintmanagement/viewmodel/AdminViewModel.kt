package com.acm431.complaintmanagement.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.acm431.complaintmanagement.model.Complaint
import com.google.firebase.firestore.FirebaseFirestore

class AdminViewModel : ViewModel() {

    private val database = FirebaseFirestore.getInstance()
    private val collectionRef = database.collection("allComplaints")
    val complaintList = MutableLiveData<ArrayList<Complaint>>()

    init {
        retrieveData()
    }

    private fun retrieveData() {

        collectionRef.addSnapshotListener { value, error ->
            try {
                if (value != null && !value.isEmpty) {

                    val allComplaints = ArrayList<Complaint>()
                    val documents = value.documents

                    documents.forEach {
                        val complaint = it.toObject(Complaint::class.java)
                        if (complaint != null) {
                            allComplaints.add(complaint)
                        }
                    }
                    complaintList.value = allComplaints
                } else if (error != null) {

                }
            } catch (e: Exception) {

            }
        }
    }
}