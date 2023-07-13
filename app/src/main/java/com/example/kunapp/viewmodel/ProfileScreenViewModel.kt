package com.example.kunapp.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kunapp.model.Comment
import com.example.kunapp.model.MesageRow
import com.example.kunapp.model.Post
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firestore.v1.Document
import java.util.UUID

class ProfileScreenViewModel:ViewModel(){
    private val _isLoading = MutableLiveData(false)
    private val _isSuccess = MutableLiveData("notFollowed")
    private val _posts = MutableLiveData(listOf<Post>())
    private val _isError = MutableLiveData("")
    private val _logoUrl = MutableLiveData("https://firebasestorage.googleapis.com/v0/b/kunapp-17107.appspot.com/o/images%2Fa73d61ed-3154-4f20-b2da-29444fe08057.jpg?alt=media&token=ee6515c6-3303-4d76-a24c-9c1222b0877b")


    val isLoading: LiveData<Boolean> = _isLoading
    val isSuccess: LiveData<String> = _isSuccess
    val posts: LiveData<List<Post>> = _posts
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
                    _isSuccess.value="followed"
                }else{
                    _isLoading.value=false
                    _isSuccess.value="notFollowed"
                }


            }else{
                _isLoading.value=false
                _isError.value=error?.localizedMessage
                _isError.value = ""
            }
        }
        database.collection("Post").whereEqualTo("nick",profileNick).orderBy("date", Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if (error==null){
                if(value!=null&&!value.isEmpty){
                    var documents=value.documents
                    var list= mutableListOf<Post>()
                    for (doc in documents){
                        var commentList= mutableListOf<Comment>()
                        var hashList=doc.get("commentlist") as List<HashMap<String,String>>
                        for(hash in hashList){
                            var comment= Comment(hash.get("nick")?:"Nick bulunamadı",hash.get("commenttext")?:"text bulunamadı")
                            commentList.add(comment)
                        }
                        var post=Post(
                            doc.id,
                            doc.get("nick") as String,
                            doc.get("posttext")as String,
                            doc.get("url") as String,
                            doc.get("likelist") as List<String>,
                            commentList

                        )

                        list.add(post)
                    }
                    _posts.value=list



                }
            }else{
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
                        _isSuccess.value = "followed"
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
                        _isSuccess.value = "notFollowed"
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
    fun message(userNick:String,profileNick:String){
        _isLoading.value=true
        database.collection("Chats").whereEqualTo("user1", userNick).addSnapshotListener { value, error ->
            if(error==null){
                database.collection("Chats").whereEqualTo("user2", userNick).addSnapshotListener { value2, error2 ->

                    if (error2==null){
                        var list= mutableListOf<MesageRow>()
                        val docs=if (value==null) listOf<DocumentSnapshot>() else value!!.documents +
                                if (value2==null) listOf<DocumentSnapshot>() else value2!!.documents
                        for(doc in docs){
                            val temp=doc.get("chat") as List<HashMap<String, String>>
                            val mesageRow= MesageRow(
                                doc.id,
                                doc.get("user1") as String,
                                doc.get("user2") as String,
                                doc.get("date") as Timestamp,
                                if((temp).isNotEmpty()){
                                    (temp).get((temp).size-1).get("message")!!

                                }else{
                                    ""
                                }
                                // you have to look at that at least

                            )
                            list.add(mesageRow)
                        }
                        list= list.filter { it.user1==profileNick||it.user2==profileNick } as MutableList<MesageRow>

                        _isLoading.value=false
                        if (list.size==1){
                            _isSuccess.value=list.get(0).id
                        }else{
                            _isSuccess.value="newChat"
                        }


                        //********************************************************
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
    fun startNewChat(userNick: String,profileNick: String){
        val hash=HashMap<String,Any>()
        hash.put("user1",userNick)
        hash.put("user2",profileNick)
        hash.put("chat", listOf<HashMap<String,String>>())
        hash.put("date",Timestamp.now())
        database.collection("Chats").add(hash).addOnSuccessListener {
            _isSuccess.value=it.id
        }
    }
}