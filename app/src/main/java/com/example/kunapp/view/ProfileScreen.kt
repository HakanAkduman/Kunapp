package com.example.kunapp.view

import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.kunapp.R
import com.example.kunapp.viewmodel.ProfileScreenViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun ProfileScreen(userNick:String?,profileNick:String?,navController: NavController,loginNavController: NavController,
                    viewModel:ProfileScreenViewModel= remember {
                        ProfileScreenViewModel()
                    }) {

    ProfileScreenGenerate(navController,userNick, profileNick, loginNavController, viewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreenGenerate(
    navController: NavController,
    userNick: String?,
    profileNick: String?,
    loginNavController: NavController,
    viewModel: ProfileScreenViewModel
) {
    viewModel.controlFollowings(userNick!!,profileNick!!)
    val isLoading by viewModel.isLoading.observeAsState(false)
    val isSuccess by viewModel.isSuccess.observeAsState(false)
    val isError by viewModel.isError.observeAsState("")
    val logo by viewModel.logoUrl.observeAsState("https://firebasestorage.googleapis.com/v0/b/kunapp-17107.appspot.com/o/images%2Fa73d61ed-3154-4f20-b2da-29444fe08057.jpg?alt=media&token=ee6515c6-3303-4d76-a24c-9c1222b0877b")
    var follow by remember { mutableStateOf(false) }

    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val imageLoader = ImageLoader(LocalContext.current)

    val request = ImageRequest.Builder(LocalContext.current)
        .data(imageUri.value)
        .target { result ->
            val bitmap = (result as BitmapDrawable).bitmap
        }
        .build()

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            imageUri.value = uri
            imageLoader.enqueue(request)
            imageUri.value?.let {
                viewModel.refLogo(imageUri.value!!, userNick!!)
            }
        }
    )

    val pickImageButton = {
        pickImageLauncher.launch("image/*")
    }

    follow = isSuccess

    val nick by remember { mutableStateOf(profileNick) }
    val editable = userNick == profileNick

    Column(modifier = Modifier.fillMaxSize()) {
        if (editable) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.End
            ) {
                ImageButton(
                    modifier = Modifier
                        .size(35.dp)
                        .border(BorderStroke(0.dp, Color.Transparent), CircleShape),
                    painter = painterResource(id = R.drawable.message_icon),
                    onClick = { /* Open messages */ }
                )
            }
        }

        Row(modifier = Modifier.padding(top = 50.dp, start = 20.dp, end = 20.dp, bottom = 50.dp)) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .border(2.dp, Color.Black, CircleShape)
                    .clickable(enabled = editable) { pickImageButton() }
            ) {
                Image(
                    painter = rememberAsyncImagePainter(logo),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize().border(0.dp,Color.Black, CircleShape),
                    contentScale = ContentScale.Crop,
                )
            }

            Column(modifier = Modifier.padding(start = 15.dp)) {
                Text(
                    text = nick ?: "Nick bulunamadı",
                    fontSize = 25.sp,
                    fontStyle = FontStyle.Italic
                )
                Spacer(modifier = Modifier.height(10.dp))
                if (editable) {
                    Button(
                        onClick = {
                            val mAuth = FirebaseAuth.getInstance()

                            loginNavController.navigate("login_screen") {
                                launchSingleTop = true
                                popUpTo("profile_screen/$userNick") { inclusive = true }
                            }
                            mAuth.signOut()
                        },
                        modifier = Modifier.width(120.dp)
                    ) {
                        Text(text = "Çıkış yap")
                    }
                }

                else{
                    Button(
                        onClick = {
                            if (follow) {
                                viewModel.unfollow(userNick!!, profileNick!!)
                            } else {
                                viewModel.follow(userNick!!, profileNick!!)
                            }
                        },
                        modifier = Modifier.width(120.dp)
                    ) {
                        if (follow) {
                            Text(text = "Takipten çık")
                        } else {
                            Text(text = "Takip et")
                        }
                    }
                }

            }
        }
    }
}


fun openMessages() {
    // Mesajlar ekranını açmak için gerekli işlemler
}



@Preview(showBackground = true)
@Composable
private fun ScreenPreview() {
    ProfileScreenGenerate(navController = NavController(LocalContext.current), userNick = "", profileNick = "", loginNavController = NavController(
        LocalContext.current),ProfileScreenViewModel()
    )
}
