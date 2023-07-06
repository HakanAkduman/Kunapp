package com.example.kunapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kunapp.model.MesageRow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class MessageScreenViewModel: ViewModel() {
    private val _isLoading = MutableLiveData(false)
    private val _isSuccess = MutableLiveData(listOf<MesageRow>())
    private val _isError = MutableLiveData("")

    val isLoading: LiveData<Boolean> = _isLoading
    val isSuccess: LiveData<List<MesageRow>> = _isSuccess
    val isError: LiveData<String> = _isError

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database= FirebaseFirestore.getInstance()

    fun loadMessages(userNick:String){
        _isLoading.value=true
        database.collection("Chats").whereEqualTo("user1", userNick).addSnapshotListener { value, error ->
            if(error==null){
                database.collection("Chats").whereEqualTo("user2", userNick).addSnapshotListener { value2, error2 ->

                    if (error2==null){
                        val list= mutableListOf<MesageRow>()
                        val docs=if (value==null) listOf<DocumentSnapshot>() else value!!.documents +
                                if (value2==null) listOf<DocumentSnapshot>() else value2!!.documents
                        for(doc in docs){

                            val mesageRow=MesageRow(
                                doc.id,
                                doc.get("user1") as String,
                                doc.get("user2") as String,
                                doc.get("date") as String,
                                doc.get("profileURL") as String,
                                (doc.get("chat") as List<HashMap<String, String>>).get((doc.get("chat") as List<HashMap<String, String>>).size-1).get("message")!!
                            // you have to look at that at least

                            )
                            list.add(mesageRow)
                        }
                        _isLoading.value=false
                        _isSuccess.value=list
                    }else{
                        _isLoading.value=false
                        _isError.value=error2.localizedMessage
                    }
                }
            }else{
                _isLoading.value=false
                _isError.value=error.localizedMessage
            }
        }

    }
}