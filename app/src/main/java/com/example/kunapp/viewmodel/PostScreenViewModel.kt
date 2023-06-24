package com.example.kunapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kunapp.model.Comment
import com.example.kunapp.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage

class PostScreenViewModel:ViewModel() {
    private val _isLoading = MutableLiveData(false)
    private val _isSuccess = MutableLiveData(listOf<Post>())
    private val _isError = MutableLiveData("")


    val isLoading: LiveData<Boolean> = _isLoading
    val isSuccess: LiveData<List<Post>> = _isSuccess
    val isError: LiveData<String> = _isError

    val database= FirebaseFirestore.getInstance()

    fun getPostsPopular(){
        _isLoading.value=true
        database.collection("Post").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if(error==null){
                if(value!=null&&!value.isEmpty){
                    var documents=value.documents
                    var list= mutableListOf<Post>()
                    for (doc in documents){
                        var commentList= mutableListOf<Comment>()
                        var hashList=doc.get("commentlist") as List<HashMap<String,String>>
                        for(hash in hashList){
                            var comment=Comment(hash.get("nick")?:"Nick bulunamadı",hash.get("commenttext")?:"text bulunamadı")
                            commentList.add(comment)
                        }
                        var post=Post(
                            doc.get("nick") as String,
                            doc.get("posttext")as String,
                            doc.get("url") as String,
                            doc.get("likelist") as List<String>,
                            doc.get("likenumber") as String,
                            commentList

                        )
                        println(post.postText+"  :  "+post.nick)
                        list.add(post)
                    }
                    _isLoading.value=false
                    _isSuccess.value=list



                }else{
                    _isLoading.value=false
                    _isError.value="There is nothing to show"
                    _isError.value=""
                }

            }else{
                _isLoading.value=false
                _isError.value=error!!.localizedMessage
                _isError.value=""
            }
        }

    }
    fun getPostsFollowings(){

    }
}