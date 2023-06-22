package com.example.kunapp.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kunapp.R

@Composable
fun NewPostScreen(navController: NavController){

    NewPostScreenGenerate(navController)


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewPostScreenGenerate(navController: NavController){
var postText by remember{ mutableStateOf("") }
Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.80F)
        .padding(vertical = 10.dp, horizontal = 15.dp)) {
    OutlinedTextField(
        value = postText,
        onValueChange = { postText = it },
        label = { Text(text = "Paylaşmak istediklerinizi girebilirsiniz") },
        modifier = Modifier.fillMaxWidth()
    )

    ImageButton(modifier = Modifier.size(150.dp), drawableToDraw = R.drawable.tap_to_load_image){
        takePhoto()
    }
    Button(onClick = { share() }) {
        Text(text = "Paylaş")
    }

}

}

fun share(){
    //daha sonra eklenecek
}

fun takePhoto(){
    Log.e("test","take photopart has been worked")
}
@Composable
fun ImageButton(modifier:Modifier,drawableToDraw:Int,description:String?=null,onClick: () -> Unit) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        color = Color.Transparent
    ) {
        Image(
            painter = painterResource(drawableToDraw),
            contentDescription = description,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
@Preview(showBackground = true)
@Composable
private fun ScreenPreview() {
    NewPostScreenGenerate(navController = NavController(LocalContext.current))
}