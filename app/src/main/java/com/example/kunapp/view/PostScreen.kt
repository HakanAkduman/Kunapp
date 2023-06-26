package com.example.kunapp.view

import android.content.Context
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.Shape
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.kunapp.model.Comment
import com.example.kunapp.model.Post
import com.example.kunapp.viewmodel.PostScreenViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseApp.initializeApp
import com.google.firebase.analytics.FirebaseAnalytics
import java.util.Locale


lateinit var viewModel: PostScreenViewModel

@Composable
fun PostScreen(nick:String?,navController: NavController,loginNavController: NavController){
    println("post screene gelen nick= $nick")
    viewModel= remember{ PostScreenViewModel() }


    PostScreenGenerate(navController,nick,loginNavController)
    viewModel.getPostsPopular()

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PostScreenGenerate(navController: NavController,nick: String?,loginNavController: NavController){


    val isLoading by viewModel.isLoading.observeAsState(false)
    val isSuccess by viewModel.isSuccess.observeAsState(listOf())
    val isError by viewModel.isError.observeAsState("")
    val searchItems by viewModel.searchList.observeAsState(initial = listOf())

    var search by remember{
        mutableStateOf("")
    }

    var clickedIndex by remember{
        mutableStateOf(0)
    }
    var postList by remember{
        mutableStateOf(listOf<Post>())
    }
    if (isSuccess.isNotEmpty()){

        postList= viewModel.isSuccess.value!!

    }



    Column(modifier = Modifier.fillMaxSize()
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


        Box {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(
                    onClick = {
                        clickedIndex = 0
                        viewModel.getPostsPopular()
                    },
                    colors = if (clickedIndex == 0) ButtonDefaults.buttonColors(Color.Red) else ButtonDefaults.buttonColors(
                        Color.Cyan
                    )
                ) {
                    Text(text = "popüler")
                }


                Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.padding(5.dp)) {

                    OutlinedTextField(
                        value = search,modifier = Modifier.size(width = 150.dp, height = 35.dp),
                        onValueChange = {
                            search= it.lowercase(Locale.UK)

                            viewModel.searchNick(search)
                        },
                        label = { Text("Bir hesap ara", fontSize = 10.sp) }

                    )



                    if (searchItems.isNotEmpty()) {

                        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.padding(top = 35.dp)){
                            items(searchItems){
                                searchItem(item = it, navController = navController, userNick = nick?:"nick couldn't found")
                            }
                        }
                    }

                }
                Button(
                    onClick = {
                        clickedIndex = 1
                    },
                    colors = if (clickedIndex == 1) ButtonDefaults.buttonColors(Color.Red) else ButtonDefaults.buttonColors(
                        Color.Cyan
                    )
                ) {
                    Text(text = "Takipler")
                }
            }

            OutlinedTextField(
                value = search,
                onValueChange = {
                    search = it.lowercase(Locale.UK)
                    viewModel.searchNick(search)
                },
                label = { Text("Bir hesap ara", fontSize = 10.sp) },
                modifier = Modifier.fillMaxWidth().padding(top = 35.dp, start = 16.dp, end = 16.dp)
            )

            if (searchItems.isNotEmpty()) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(top = 35.dp)
                ) {
                    items(searchItems) { item ->
                        searchItem(item = item, navController = navController, userNick = nick ?: "")
                    }
                }
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            LazyColumn {
                items(items = postList) { post ->
                    PostItem(post = post)
                }
            }
        }

    }

}

@Composable
fun searchItem(item: String, navController: NavController, userNick: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("profile_screen/$userNick/$item")
            }
            .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
            .border(1.dp, Color.Black, RectangleShape)
    ) {
        Text(
            item,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun PostItem(post: Post) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 7.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = post.nick, style = MaterialTheme.typography.titleMedium)
            Text(text = post.postText, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 8.dp))
            if (!post.imageUri.isNullOrBlank()) {
                Image(
                    painter = rememberAsyncImagePainter(model = post.imageUri),
                    contentDescription = "photo of post",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
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
}

@Preview(showSystemUi = true,showBackground = true)
@Composable
private fun ScreenPreview() {

    viewModel= remember{ PostScreenViewModel() }

    PostScreenGenerate(NavController(LocalContext.current),"", NavController(LocalContext.current))
    viewModel.getPostsPopular()

}