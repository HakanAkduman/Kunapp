package com.example.kunapp.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kunapp.R
import com.example.kunapp.viewmodel.ChatScreenViewModel

@Composable
fun ProfileScreen(navController: NavController) {
    ProfileScreenGenerate(navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreenGenerate(navController: NavController,viewModel:ChatScreenViewModel=remember{ ChatScreenViewModel() }) {
    val isLoading by viewModel.isLoading.observeAsState(false)
    val isSuccess by viewModel.isSuccess.observeAsState("")
    val isError by viewModel.isError.observeAsState("")
   
    var nick by remember { mutableStateOf("nick") }
    if(isLoading==true){
        Box(modifier = Modifier.fillMaxSize()){
            CircularProgressIndicator()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
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
                drawableToDraw = R.drawable.message_icon
            ) {
                navController.navigate("message_screen/$nick")
            }
        }
        Row(modifier = Modifier.padding(top = 50.dp, start = 20.dp, end = 20.dp, bottom = 50.dp)) {
            ImageButton(
                modifier = Modifier
                    .size(100.dp)
                    .border(2.dp, Color.Black, CircleShape),
                drawableToDraw = R.drawable.default_profile_photo
            ) {
                changeProfile()
            }
            Column(modifier = Modifier.padding(start = 15.dp)) {
                Text(
                    text = nick,
                    fontSize = 25.sp,
                    fontStyle = FontStyle.Italic
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.width(120.dp)
                ) {
                    Text(text = "Takip et", fontSize = 14.sp)
                }
            }
        }
    }
}



fun changeProfile() {
    // Profil fotoğrafını değiştirmek için gerekli işlemler
}

@Preview(showBackground = true)
@Composable
private fun ScreenPreview() {
    ProfileScreenGenerate(navController = NavController(LocalContext.current))
}
