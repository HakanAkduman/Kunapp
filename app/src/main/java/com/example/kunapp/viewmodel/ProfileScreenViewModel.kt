package com.example.kunapp.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class ProfileScreenViewModel:ViewModel(){
    private val _isLoading = MutableLiveData(false)
    private val _isSuccess = MutableLiveData(false)
    private val _isError = MutableLiveData("")
    private val _logoUrl = MutableLiveData("https://firebasestorage.googleapis.com/v0/b/kunapp-17107.appspot.com/o/images%2Fa73d61ed-3154-4f20-b2da-29444fe08057.jpg?alt=media&token=ee6515c6-3303-4d76-a24c-9c1222b0877b")


    val isLoading: LiveData<Boolean> = _isLoading
    val isSuccess: LiveData<Boolean> = _isSuccess
    val isError: LiveData<String> = _isError
    val logoUrl: LiveData<String> = _logoUrl


    val database=FirebaseFirestore.getInstance()
    val storage=FirebaseStorage.getInstance()

    fun controlFollowings(userNick:String,profileNick:String){
        _isLoading.value=true
        database.collection("Users").whereEqualTo("nick",userNick).addSnapshotListener { value, error ->
            if (error==null&&value!=null &&!value.isEmpty){
                val document= value.documents[0]
                database.collection("Users").whereEqualTo("nick",profileNick).get().addOnSuccessListener {
                    _logoUrl.value=it.documents.get(0).get("photoUrl") as String
                }

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
    fun refLogo(uri: Uri,nick:String){
        val uuid= UUID.randomUUID()
        _isLoading.value=true
        storage.reference.child("images").child("${uuid}.jpg").putFile(uri).addOnSuccessListener {
            val UrlTask=storage.reference.child("images").child("${uuid}.jpg").downloadUrl
                .addOnSuccessListener {
                    var url=it.toString()
                    database.collection("Users").whereEqualTo("nick",nick).get().addOnSuccessListener {
                        val id=it.documents.get(0).id
                        database.document("Users/$id").update("photoUrl",url).addOnFailureListener {
                            _isLoading.value=false
                            _isError.value=it.localizedMessage
                            _isError.value=""
                        }.addOnSuccessListener {
                            _isLoading.value=false
                            _logoUrl.value=url

                        }

                    }.addOnFailureListener {
                        _isLoading.value=false
                        _isError.value=it.localizedMessage
                        _isError.value=""
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
}