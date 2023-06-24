package com.example.kunapp.model

import android.net.Uri

data class Post(
    val nick:String,
    val postText:String,
    val imageUri: String,
    var likeList: List<String>,
    var likeNumber:String,
    var commentList:List<Comment>
) {
}