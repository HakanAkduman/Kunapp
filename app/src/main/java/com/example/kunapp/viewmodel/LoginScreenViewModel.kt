package com.example.kunapp.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class LoginScreenViewModel():ViewModel() {
    private val _isLoading = MutableLiveData(false)
    private val _isSuccess = MutableLiveData("")
    private val _isError = MutableLiveData("")


    val isLoading: LiveData<Boolean> = _isLoading
    val isSuccess: LiveData<String> = _isSuccess
    val isError: LiveData<String> = _isError

    private val database= FirebaseFirestore.getInstance()
    private var auth:FirebaseAuth = FirebaseAuth.getInstance()

    fun Login(email:String,password:String,navController: NavController){
        _isLoading.value=true
        println("fonksiyona girildi")
        auth.signInWithEmailAndPassword(email,password).addOnFailureListener {
            _isLoading.value=false
            _isError.value=it.localizedMessage
            _isError.value = ""
        }.addOnSuccessListener {
            database.collection("Users").whereEqualTo("email",email).addSnapshotListener { value, error ->
                if (error==null&&value!=null &&!value.isEmpty){
                    _isLoading.value=false
                    _isSuccess.value=value.documents[0].get("nick") as String
                }else{
                    _isLoading.value=false
                    _isError.value=error?.localizedMessage
                    _isError.value = ""
                }
            }

        }
    }



}