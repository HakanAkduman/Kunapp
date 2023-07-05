package com.example.kunapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class ProfileScreenViewModel:ViewModel(){
    private val _isLoading = MutableLiveData(false)
    private val _isSuccess = MutableLiveData(false)
    private val _isError = MutableLiveData("")


    val isLoading: LiveData<Boolean> = _isLoading
    val isSuccess: LiveData<Boolean> = _isSuccess
    val isError: LiveData<String> = _isError

    val database=FirebaseFirestore.getInstance()

    fun controlFollowings(userNick:String,profileNick:String){
        _isLoading.value=true
        database.collection("Users").whereEqualTo("nick",userNick).addSnapshotListener { value, error ->
            if (error==null&&value!=null &&!value.isEmpty){
                val document= value.documents[0]
                val mArray=document.get("followings") as List<String>
                if(mArray.contains(profileNick)){
                    _isLoading.value=false
                    _isSuccess.value=true
                }else{
                    _isLoading.value=false
                    _isSuccess.value=false
                }


            }else{
                _isLoading.value=false
                _isError.value=error?.localizedMessage
                _isError.value = ""
            }
        }
    }
    fun follow(userNick: String, profileNick: String) {
        _isLoading.value = true
        database.collection("Users").whereEqualTo("nick", userNick).get().addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                val documentId = querySnapshot.documents[0].id

                database.document("Users/$documentId").update("followings", FieldValue.arrayUnion(profileNick))
                    .addOnSuccessListener {
                        _isLoading.value = false
                        _isSuccess.value = true
                    }.addOnFailureListener {
                        _isLoading.value = false
                        _isError.value = it.localizedMessage
                    }
            } else {
                _isLoading.value = false
                _isError.value = "User not found"
            }
        }.addOnFailureListener {
            _isLoading.value = false
            _isError.value = it.localizedMessage
        }
    }
    fun unfollow(userNick: String, profileNick: String) {
        _isLoading.value = true
        database.collection("Users").whereEqualTo("nick", userNick).get().addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                val documentId = querySnapshot.documents[0].id

                database.document("Users/$documentId").update("followings", FieldValue.arrayRemove(profileNick))
                    .addOnSuccessListener {
                        _isLoading.value = false
                        _isSuccess.value = false
                    }.addOnFailureListener {
                        _isLoading.value = false
                        _isError.value = it.localizedMessage
                    }
            } else {
                _isLoading.value = false
                _isError.value = "User not found"
            }
        }.addOnFailureListener {
            _isLoading.value = false
            _isError.value = it.localizedMessage
        }
    }
}