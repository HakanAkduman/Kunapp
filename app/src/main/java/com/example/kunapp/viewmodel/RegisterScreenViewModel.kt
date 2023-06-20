package com.example.kunapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

class RegisterScreenViewModel:ViewModel() {
    private val _isLoading = MutableLiveData(false)
    private val _isSuccess = MutableLiveData(false)
    private val _isError = MutableLiveData("")

    val isLoading: LiveData<Boolean> = _isLoading
    val isSuccess: LiveData<Boolean> = _isSuccess
    val isError: LiveData<String> = _isError

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun register(email: String, password: String, nick: String) {
        _isLoading.value = true

        auth.createUserWithEmailAndPassword(email, password)
            .addOnFailureListener { exception ->
                _isLoading.value = false
                _isError.value = exception.localizedMessage
            }
            .addOnSuccessListener {
                _isLoading.value = false
                _isSuccess.value = true

            }
            .addOnCompleteListener {
                _isLoading.value = false
            }
    }
}