package com.example.kunapp.model

import android.net.Uri

data class Post(
    val nick:String,
    val postText:String,
    val imageUri: Uri?,
    var likeList: List<String>,
    var commentList:List<Comment>
) {
}