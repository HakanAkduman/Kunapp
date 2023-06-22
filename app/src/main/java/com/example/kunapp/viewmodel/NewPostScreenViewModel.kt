package com.example.kunapp.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewPostScreenViewModel:ViewModel() {
    private val _isLoading = MutableLiveData(false)
    private val _isSuccess = MutableLiveData("")
    private val _isError = MutableLiveData("")


    val isLoading: LiveData<Boolean> = _isLoading
    val isSuccess: LiveData<String> = _isSuccess
    val isError: LiveData<String> = _isError

    fun share(text:String,photoUri: Uri?=null){

    }

}