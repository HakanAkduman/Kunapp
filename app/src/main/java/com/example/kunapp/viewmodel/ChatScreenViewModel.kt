package com.example.kunapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kunapp.model.Message
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.reflect.Field
import java.sql.Timestamp

class ChatScreenViewModel : ViewModel() {
    private val _isLoading = MutableLiveData(false)
    private val _isSuccess = MutableLiveData(mutableListOf<Message>())
    private val _isError = MutableLiveData("")


    val isLoading: LiveData<Boolean> = _isLoading
    val isSuccess: LiveData<MutableList<Message>> = _isSuccess
    val isError: LiveData<String> = _isError

    private val database = FirebaseFirestore.getInstance()
    fun sendMessage(nick: String, sendmessage: String, messageId: String) {
        var hash = HashMap<String, String>()
        hash.put("sender", nick)
        hash.put("message", sendmessage)
        database.document("Chats/$messageId").update("chat", FieldValue.arrayUnion(hash))
            .addOnSuccessListener {

                database.document("Chats/$messageId")
                    .update("date", com.google.firebase.Timestamp.now().toString())
                    .addOnFailureListener {
                        _isError.value = it.localizedMessage
                    }.addOnSuccessListener {
                        _isSuccess.value?.add(Message(nick,sendmessage))
                    }

            }.addOnFailureListener { _isError.value = it.localizedMessage }

    }

    fun openMessages(id: String) {
        _isLoading.value = true
        database.document("Chats/$id").get().addOnSuccessListener {
            if (it != null) {
                val list = it.get("chat") as? List<HashMap<String, String>>
                val messagelist = mutableListOf<Message>()
                list.let {
                    for (doc in list!!) {
                        val message = Message(doc.get("sender")!!, doc.get("message")!!)

                        messagelist.add(message)
                    }
                }


                _isLoading.value = false
                _isSuccess.value = messagelist
            }
            _isLoading.value = false
        }.addOnFailureListener {
            _isLoading.value = false
            _isError.value = it.localizedMessage

        }.addOnCompleteListener {
            _isLoading.value = false
        }


    }

}