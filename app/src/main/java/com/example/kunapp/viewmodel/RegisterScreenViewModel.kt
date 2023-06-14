package com.example.kunapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class RegisterScreenViewModel():ViewModel() {
    var isLoading= mutableStateOf(false)
    var isSuccess= mutableStateOf(false)
    var isError= mutableStateOf(false)

    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun Register(email:String,password:String){
        isLoading.value=true
        auth.createUserWithEmailAndPassword(email,password).addOnFailureListener {
            isLoading.value=false
            isError.value=true
        }.addOnSuccessListener {
            isLoading.value=false
            isSuccess.value=true
        }
    }
}