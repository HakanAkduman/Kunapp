package com.example.kunapp.view

import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.Shape
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.kunapp.model.Comment
import com.example.kunapp.model.Post
import com.example.kunapp.viewmodel.PostScreenViewModel


lateinit var viewModel: PostScreenViewModel

@Composable
fun PostScreen(nick:String?,navController: NavController){
    viewModel= remember{ PostScreenViewModel() }

    PostScreenGenerate(navController,nick)
    viewModel.getPostsPopular()

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PostScreenGenerate(navController: NavController,nick: String?){


    val isLoading by viewModel.isLoading.observeAsState(false)
    val isSuccess by viewModel.isSuccess.observeAsState(listOf())
    val isError by viewModel.isError.observeAsState("")

    var clickedIndex by remember{
        mutableStateOf(0)
    }
    var postList by remember{
        mutableStateOf(listOf<Post>())
    }
    if (isSuccess.isNotEmpty()){
        println(viewModel.isSuccess.value!![0].nick)
        println(viewModel.isSuccess.value!![0].postText)
        postList= viewModel.isSuccess.value!!

    }



    Column(modifier = Modifier.fillMaxWidth()
    ) {

        if (isLoading) {//daha sonra loading screen eklenebilir

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(60.dp)
                        .padding(16.dp),
                    color = Color.White
                )
            }
        }
        if (isError.isNotEmpty()) {
            Toast.makeText(LocalContext.current, isError, Toast.LENGTH_LONG).show()

        }


        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {


            Button(onClick = {
                clickedIndex=0
                viewModel.getPostsPopular()
            },
                colors =if (clickedIndex==0) ButtonDefaults.buttonColors(Color.Red) else ButtonDefaults.buttonColors(
                    Color.Cyan)
            ) {
                Text(text = "popüler")
            }
            Button(onClick = {

                clickedIndex=1

                             },
                colors =if (clickedIndex==1) ButtonDefaults.buttonColors(Color.Red) else ButtonDefaults.buttonColors(Color.Cyan)
                ) {
                Text(text = "Takipler")
            }



        }

        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            items(items = postList) { post ->
                println(post.nick+"  ::  "+post.postText)
                PostItem(post = post)
            }
        }

    }

}
@Composable
fun PostItem(post: Post) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 7.dp)
            .background(Color.LightGray, RectangleShape).border(1.dp,Color.DarkGray,
                RectangleShape)
    ) {
        Text(
            modifier = Modifier.padding(start = 5.dp, bottom = 5.dp),
            text = post.nick
        )
        Text(text = post.postText)
        if (!post.imageUri.isNullOrBlank()) {
            Image(
                painter = rememberAsyncImagePainter(model = post.imageUri),
                contentDescription = "photo of post",
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentScale = ContentScale.Fit
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { /* TODO: Beğen action */ }) {
                Text(text = "Beğen")
            }
            Button(onClick = { /* TODO: Yorum action */ }) {
                Text(text = "Yorum")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenPreview() {
    PostScreenGenerate(navController = NavController(LocalContext.current),"")
}