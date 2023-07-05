package com.example.kunapp.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegisterScreenViewModel:ViewModel() {
    private val _isLoading = MutableLiveData(false)
    private val _isSuccess = MutableLiveData(false)
    private val _isError = MutableLiveData("")

    val isLoading: LiveData<Boolean> = _isLoading
    val isSuccess: LiveData<Boolean> = _isSuccess
    val isError: LiveData<String> = _isError

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database=FirebaseFirestore.getInstance()

    fun register(email: String, password: String, nick: String) {
        _isError.value=""
        _isLoading.value = true

        database.collection("Users").whereEqualTo("nick",nick).addSnapshotListener{ value, error ->


            if(error==null){
                if(value==null || value.isEmpty){
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnFailureListener { exception ->
                            _isLoading.value = false
                            _isError.value = exception.localizedMessage
                            Log.e("Error",exception.localizedMessage!!)
                        }
                        .addOnSuccessListener {
                            var hash=HashMap<String,Any>()
                            hash.put("nick",nick)
                            hash.put("email",email)
                            hash.put("followings", listOf<String>())
                            hash.put("chats", listOf<String>())
                            database.collection("Users").add(hash).addOnSuccessListener {
                                _isLoading.value = false
                                _isSuccess.value = true
                            }.addOnFailureListener {
                                _isLoading.value = false
                                _isError.value = it.localizedMessage
                                Log.e("Error",it.localizedMessage!!)
                            }.addOnCanceledListener {
                                _isLoading.value = false
                            }


                        }
                        .addOnCompleteListener {
                            _isLoading.value = false
                        }

                }else{
                    _isLoading.value = false
                    _isError.value="Bu nick daha önceden alınmış"
                    Log.e("Error","Bu nick daha önceden alınmış")
                }
            }else{
                _isLoading.value = false
                _isError.value=error.localizedMessage
                Log.e("Error",error.localizedMessage!!)
            }
        }


    }

}