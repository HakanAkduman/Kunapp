package com.example.kunapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class ChatScreenViewModel:ViewModel() {
    private val _isLoading = MutableLiveData(false)
    private val _isSuccess = MutableLiveData("")
    private val _isError = MutableLiveData("")


    val isLoading: LiveData<Boolean> = _isLoading
    val isSuccess: LiveData<String> = _isSuccess
    val isError: LiveData<String> = _isError

    private val database= FirebaseFirestore.getInstance()
    fun openMessages(nick:String){
        _isLoading.value=true
        database.collection("Users").whereEqualTo("nick",nick).addSnapshotListener { value, error ->
            if(error==null&&value!=null&&!value.isEmpty){

            }
            else{
                _isLoading.value=false

            }
        }


    }

}