package com.example.kunapp.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth


class LoginScreenViewModel():ViewModel() {
    var isLoading= mutableStateOf(false)
    var isSuccess= mutableStateOf(false)
    var isError= mutableStateOf(false)

    var auth:FirebaseAuth = FirebaseAuth.getInstance()

    fun Login(email:String,password:String,navController: NavController){
        isLoading.value=true
        println("fonksiyona girildi")
        auth.signInWithEmailAndPassword(email,password).addOnFailureListener {
            isLoading.value=false
            isError.value=true
        }.addOnSuccessListener {
            isLoading.value=false
            isSuccess.value=true
            navController.navigate("main_screen")
        }
    }



}