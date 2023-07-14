package com.example.kunapp.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kunapp.model.Comment
import com.example.kunapp.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage

class PostScreenViewModel:ViewModel() {
    private val _isLoading = MutableLiveData(false)
    private val _isSuccess = MutableLiveData(listOf<Post>())
    private val _searchList = MutableLiveData(listOf<String>())
    private val _isError = MutableLiveData("")
    private val allNicks=MutableLiveData(listOf<String>())


    val isLoading: LiveData<Boolean> = _isLoading
    val isSuccess: LiveData<List<Post>> = _isSuccess
    val searchList:LiveData<List<String>> =_searchList
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
                            doc.id,
                            doc.get("nick") as String,
                            doc.get("posttext")as String,
                            doc.get("url") as String,
                            doc.get("likelist") as List<String>,
                            commentList

                        )

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
    fun getPostsFollowings(nick: String) {
        _isLoading.value = true

        database.collection("Users").whereEqualTo("nick", nick).addSnapshotListener { userValue, userError ->
            if (userError == null) {
                val followList = userValue?.documents?.get(0)?.get("followings") as? List<String>
                if (followList != null) {
                    database.collection("Post").whereIn("nick", followList).addSnapshotListener { postValue, postError ->
                        if (postError == null) {
                            if (postValue != null && !postValue.isEmpty) {
                                val documents = postValue.documents
                                val list = mutableListOf<Post>()
                                for (doc in documents) {
                                    val commentList = mutableListOf<Comment>()
                                    val hashList = doc.get("commentlist") as? List<HashMap<String, String>>
                                    if (hashList != null) {
                                        for (hash in hashList) {
                                            val comment = Comment(
                                                hash["nick"] ?: "Nick bulunamadı",
                                                hash["commenttext"] ?: "text bulunamadı"
                                            )
                                            commentList.add(comment)
                                        }
                                    }
                                    val post = Post(
                                        doc.id,
                                        doc.getString("nick") ?: "",
                                        doc.getString("posttext") ?: "",
                                        doc.getString("url") ?: "",
                                        doc.get("likelist") as? List<String> ?: emptyList(),
                                        commentList
                                    )
                                    list.add(post)
                                }
                                _isLoading.value = false
                                _isSuccess.value = list
                            } else {
                                _isLoading.value = false
                                _isError.value = "There is nothing to show"
                            }
                        } else {
                            _isLoading.value = false
                            _isError.value = postError.localizedMessage
                            _isError.value = ""
                        }
                    }
                }
            } else {
                _isLoading.value = false
                _isError.value = userError.localizedMessage
                _isError.value = ""
            }
        }
    }

    fun removePost(id:String){
        database.document("Post/$id").delete().addOnSuccessListener {
            _isSuccess.value= listOf()
            getPostsPopular()
        }.addOnFailureListener {
            Log.e("Error",it.localizedMessage)
        }
    }
    fun searchNick(str:String){
        if (str.isBlank()){
            _searchList.value= listOf<String>()
        }
        else if (allNicks.value!=null && allNicks.value!!.isNotEmpty()){
            _searchList.value= allNicks.value!!.filter { it.contains(str, ignoreCase = true) }
        }else{
            database.collection("Users").addSnapshotListener { value, error ->
                if(error!=null){
                    _isError.value=error!!.localizedMessage
                    _isError.value=""
                }else{
                    if(value!=null && !value.isEmpty){
                        var documents=value.documents
                        var list= mutableListOf<String>()
                        for (doc in documents){
                            list.add((doc.get("nick") ?:"") as String)
                        }
                        allNicks.value=list
                        searchNick(str)
                    }
                }
            }
        }

    }

    fun like(id:String,nick:String,likeList:List<String>){
        database.document("Post/$id").update("likelist", FieldValue.arrayUnion(nick)).addOnFailureListener {
            _isError.value=it.localizedMessage
        }
    }
    fun unLike(id:String,nick:String,likeList:List<String>){
        database.document("Post/$id").update("likelist", FieldValue.arrayRemove(nick)).addOnFailureListener {
            _isError.value=it.localizedMessage
        }
    }
    fun shareComment(id:String,nick:String,comment:String){
        val commentHashMap=HashMap<String,String>()
        commentHashMap.put("nick",nick)
        commentHashMap.put("commenttext",comment)
        database.document("Post/$id").update("commentlist",FieldValue.arrayUnion(commentHashMap)).addOnFailureListener {
            _isError.value=it.localizedMessage
        }
    }

}