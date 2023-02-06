package com.acm431.complaintmanagement.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    var loginResult         = MutableLiveData<Boolean>()
    val loginLoading        = MutableLiveData<Boolean>()
    val registerLoading     = MutableLiveData<Boolean>()
    val registerError       = MutableLiveData<Boolean>()
    val loginError          = MutableLiveData<Boolean>()
    val registrationSucces  = MutableLiveData<Boolean>()
    val errorMessage        = MutableLiveData<String>()

    fun login(email: String, password: String) {

        loginLoading.value = true

        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener { result ->
            loginLoading.value = false
            loginResult.value = true
        }
            .addOnFailureListener {
                println("login failed")
                loginError.value = true
                loginResult.value = false
                errorMessage.value = it.localizedMessage
                loginLoading.value = false
            }
    }

    fun register(user: com.acm431.complaintmanagement.model.User) {
        registrationSucces.value = false
        registerLoading.value = true
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    registrationSucces.value = true
                    registerLoading.value = false

                    db.collection("users").document(user.email).set(user)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "DocumentSnapshot added with ID: ${user.email}")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }
                }
            }
            .addOnFailureListener {
                errorMessage.value = it.localizedMessage
                registerLoading.value = false
                registerError.value = true
            }
    }
}