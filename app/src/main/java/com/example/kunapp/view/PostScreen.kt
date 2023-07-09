package com.example.kunapp.view

import android.app.Dialog
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.kunapp.R
import com.example.kunapp.model.Comment
import com.example.kunapp.model.Post
import com.example.kunapp.ui.theme.SoftBlue
import com.example.kunapp.viewmodel.PostScreenViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import java.util.Collections.addAll
import java.util.Locale




@Composable
fun PostScreen(nick:String?,navController: NavController,loginNavController: NavController,viewModel: PostScreenViewModel=remember{PostScreenViewModel()}){



    PostScreenGenerate(navController,nick,loginNavController,viewModel)
    viewModel.getPostsPopular()

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PostScreenGenerate(navController: NavController, nick: String?, loginNavController: NavController, viewModel: PostScreenViewModel) {

    val isLoading by viewModel.isLoading.observeAsState(false)
    val isSuccess by viewModel.isSuccess.observeAsState(listOf())
    val isError by viewModel.isError.observeAsState("")
    val searchItems by viewModel.searchList.observeAsState(initial = listOf())
    var prevError by remember { mutableStateOf("") }

    var search by remember {
        mutableStateOf("")
    }

    var clickedIndex by remember {
        mutableStateOf(0)
    }
    var postList by remember {
        mutableStateOf(listOf<Post>())
    }
    if (isSuccess.isNotEmpty()) {
        postList = viewModel.isSuccess.value!!
    }

   Box() {
       Column(modifier = Modifier.fillMaxSize()) {
           if (isLoading) {
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
           if (isError.isNotEmpty() && prevError != isError) {
               Toast.makeText(LocalContext.current, isError, Toast.LENGTH_LONG).show()
               prevError = isError
           }

           Row(
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(horizontal = 16.dp, vertical = 8.dp),
               horizontalArrangement = Arrangement.SpaceBetween,
               verticalAlignment = Alignment.CenterVertically
           ) {
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

           Box(modifier = Modifier.weight(1f)) {
               Column {


                   LazyColumn(
                       modifier = Modifier.weight(1f)
                   ) {
                       items(items = postList) { post ->
                           PostItem(post = post, viewModel = viewModel, nick = nick!!, navController = navController)
                       }
                   }
               }
           }
       }
       Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top) {
           OutlinedTextField(
               value = search,
                modifier = Modifier
                    .padding(start = 120.dp, end = 120.dp, top = 5.dp)
                    .size(170.dp, 50.dp), textStyle = TextStyle(fontSize = 13.sp),
               onValueChange = {
                   search = it.lowercase(Locale.UK)
                   viewModel.searchNick(search)
               },
               label = { Text("Bir hesap ara", fontSize = 10.sp) }
           )
           if (searchItems.isNotEmpty()) {
               LazyColumn(

               ) {
                   items(searchItems) { item ->
                       SearchItem(item = item, navController = navController, userNick = nick ?: "nick couldn't found")
                   }
               }
           }
       }
   }
}

@Composable
fun SearchItem(item: String, navController: NavController, userNick: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("profile_screen/$userNick/$item")
            }
            .padding(start = 120.dp, top = 8.dp, bottom = 8.dp, end = 120.dp)
            .border(1.dp, Color.Black, RectangleShape)
            .background(Color.LightGray)
    ) {
        Text(
            item,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(3.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDialog(
    post: Post,
    viewModel: PostScreenViewModel,
    nick: String,
    onDismissRequest: () -> Unit
) {
    var comment by remember { mutableStateOf("") }
    var commentList = remember {
        mutableListOf<Comment>().apply {
            addAll(post.commentList)
        }
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
        content = {
            var liked by remember { mutableStateOf(post.likeList.contains(nick)) }
            var likeNumber by remember { mutableStateOf(post.likeList.size) }

            Card(colors = CardDefaults.cardColors(containerColor = SoftBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 7.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(3.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = post.nick, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = post.postText,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
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
                        Button(onClick = {
                            if (!liked) {
                                viewModel.like(post.id, nick, post.likeList)
                                likeNumber += 1
                                liked = true
                            } else {
                                viewModel.unLike(post.id, nick, post.likeList)
                                likeNumber -= 1
                                liked = false
                            }
                        }) {
                            Text(text = if (liked) "($likeNumber) Beğenildi" else "($likeNumber) Beğen")
                        }
                    }
                    LazyColumn(
                        modifier = Modifier
                            .padding(vertical = 10.dp, horizontal = 20.dp)
                            .size(DpSize(width = 300.dp, height = 270.dp)),
                        contentPadding = PaddingValues(top = 8.dp)
                    ) {
                        items(commentList) { comment ->
                            Text(text = comment.nick)
                            Text(text = comment.commentText)
                        }
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = comment,
                            onValueChange = { comment = it },
                            label = { Text(text = "Yorumunuzu giriniz") },
                            modifier = Modifier.weight(1f)
                        )
                        Button(modifier = Modifier,onClick = {
                            if (comment.isNotBlank()) {
                                val newComment = Comment(nick = nick, commentText = comment)
                                commentList.add(newComment)
                                viewModel.shareComment(post.id, nick, comment)
                                comment = ""
                            }
                        }) {
                            Text(text = "Paylaş")
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostItem(
    post: Post,
    viewModel: PostScreenViewModel,
    nick: String,navController:NavController
) {
    var liked by remember { mutableStateOf(post.likeList.contains(nick)) }
    var likeNumber by remember { mutableStateOf(post.likeList.size) }
    var commentClicked by remember { mutableStateOf(false) }

    Card(colors = CardDefaults.cardColors(containerColor = SoftBlue),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 7.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            TextAsButton(text = post.nick, textStyle  = MaterialTheme.typography.titleMedium, color = SoftBlue, modifier = Modifier){
                navController.navigate("profile_screen/$nick/${post.nick}")
            }
            Text(
                text = post.postText,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
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
                Button(onClick = {
                    if (!liked) {
                        viewModel.like(post.id, nick, post.likeList)
                        likeNumber += 1
                        liked = true
                    } else {
                        viewModel.unLike(post.id, nick, post.likeList)
                        likeNumber -= 1
                        liked = false
                    }
                }) {
                    Text(text = if (liked) "($likeNumber) Beğenildi" else "($likeNumber) Beğen")
                }
                Button(onClick = {
                    commentClicked = !commentClicked
                }) {
                    Text(text = "Yorum")
                }
            }

            if (commentClicked) {
                PostDialog(
                    post = post,
                    viewModel = viewModel,
                    nick = nick,
                    onDismissRequest = { commentClicked = false }
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextAsButton(modifier:Modifier,text:String,textStyle: TextStyle,color: Color,onClick:()->Unit){
    Surface(modifier = modifier.background(color), onClick = onClick) {
        Text(text = text,modifier=Modifier.background(color), style = textStyle)
    }

}
@Preview(showSystemUi = true,showBackground = true)
@Composable
private fun ScreenPreview() {



    PostScreenGenerate(NavController(LocalContext.current),"", NavController(LocalContext.current),PostScreenViewModel())


}