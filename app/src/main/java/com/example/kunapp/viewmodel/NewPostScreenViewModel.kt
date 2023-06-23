package com.example.kunapp.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.sql.Time
import java.sql.Timestamp
import java.util.UUID

class NewPostScreenViewModel:ViewModel() {
    private val _isLoading = MutableLiveData(false)
    private val _isSuccess = MutableLiveData("")
    private val _isError = MutableLiveData("")


    val isLoading: LiveData<Boolean> = _isLoading
    val isSuccess: LiveData<String> = _isSuccess
    val isError: LiveData<String> = _isError

    val database=FirebaseFirestore.getInstance()
    val storage=FirebaseStorage.getInstance()
    val auth=FirebaseAuth.getInstance()
    fun share(nick:String?,text:String,photoUri: Uri?=null){
        val uuid=UUID.randomUUID()
        _isLoading.value=true
        if(photoUri!=null){
            storage.reference.child("images").child("${uuid}.jpg").putFile(photoUri).addOnSuccessListener {
                val UrlTask=storage.reference.child("images").child("${uuid}.jpg").downloadUrl
                    .addOnSuccessListener {
                        var url=it.toString()
                        var senderName= nick ?: auth.currentUser!!.email
                        var date=com.google.firebase.Timestamp.now()
                        var hash=HashMap<String,Any>()
                        hash.put("nick",senderName ?: "name")
                        hash.put("posttext",text)
                        hash.put("url",url)
                        hash.put("likelist", listOf<String>())
                        hash.put("commentlist",HashMap<String,String>())
                        database.collection("Post").add(hash).addOnFailureListener {
                            _isLoading.value=false
                            _isError.value=it.localizedMessage
                            _isError.value=""
                        }.addOnSuccessListener {
                            _isLoading.value=false
                            _isSuccess.value="Done"
                            _isSuccess.value=""
                        }




                    }.addOnFailureListener{
                        _isLoading.value=false
                        _isError.value=it.localizedMessage
                        _isError.value=""
                    }

            }.addOnFailureListener{
                _isLoading.value=false
                _isError.value=it.localizedMessage
                _isError.value=""
            }
        }
        else{
            var url=""
            var senderName= nick ?: auth.currentUser!!.email
            var date=com.google.firebase.Timestamp.now()
            var hash=HashMap<String,Any>()
            hash.put("nick",senderName ?: "name")
            hash.put("posttext",text)
            hash.put("url",url)
            hash.put("likelist", listOf<String>())
            hash.put("commentlist",HashMap<String,String>())
            database.collection("Post").add(hash).addOnFailureListener {
                _isLoading.value=false
                _isError.value=it.localizedMessage
                _isError.value=""
            }.addOnSuccessListener {
                _isLoading.value=false
                _isSuccess.value="Done"
                _isSuccess.value=""
            }
        }
    }

}