package com.example.kunapp.view

import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.request.ImageRequest
import com.example.kunapp.R
import com.example.kunapp.viewmodel.NewPostScreenViewModel


@Composable
fun NewPostScreen(nick:String?,navController: NavController){

    NewPostScreenGenerate(nick,navController)


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewPostScreenGenerate(nick:String?,navController: NavController,viewModel: NewPostScreenViewModel =remember{NewPostScreenViewModel()}){

    val isLoading by viewModel.isLoading.observeAsState(false)
    val isSuccess by viewModel.isSuccess.observeAsState("")
    val isError by viewModel.isError.observeAsState("")

    var postText by remember{ mutableStateOf("") }
    val context = LocalContext.current
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val imageLoader = ImageLoader(context)
    var painter :Painter? by remember {
        mutableStateOf(null)
    }


    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? -> imageUri.value = uri }
    )
    val pickImageButton = {
        pickImageLauncher.launch("image/*")

    }
    val request = ImageRequest.Builder(context)
        .data(imageUri.value)
        .target { result ->
            val bitmap = (result as BitmapDrawable).bitmap
            painter = BitmapPainter(bitmap.asImageBitmap())
        }
        .build()

    if (imageUri.value != null) {
        imageLoader.enqueue(request)
    } else {
        painter= painterResource(id = R.drawable.tap_to_load_image)
    }






Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.80F)
        .padding(vertical = 10.dp, horizontal = 15.dp)) {

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
    if (!isSuccess.isNullOrBlank()){
        navController.navigate("post_screen/$nick") {
            launchSingleTop = true
        }
    }

    OutlinedTextField(
        value = postText,
        onValueChange = { postText = it },
        label = { Text(text = "Paylaşmak istediklerinizi girebilirsiniz") },
        modifier = Modifier.fillMaxWidth()
    )
    ImageButton(painter = painter!!,
        description = "Selected Image",
        modifier = Modifier.fillMaxSize(0.60F),
        onClick = pickImageButton

    )


    Button(onClick = { viewModel.share(text = postText,photoUri=imageUri.value, nick = nick) }) {
        Text(text = "Paylaş")
    }

}

}







@Composable
fun ImageButton(modifier:Modifier,painter: Painter,description:String?=null,onClick: () -> Unit) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        color = Color.Transparent
    ) {
        Image(
            painter = painter,
            contentDescription = description,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }
}
@Preview(showBackground = true)
@Composable
private fun ScreenPreview() {
    NewPostScreenGenerate("",navController = NavController(LocalContext.current))
}