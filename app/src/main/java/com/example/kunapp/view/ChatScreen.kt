package com.example.kunapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kunapp.R


val message= mutableStateOf("")
private val bubblecorner= RoundedCornerShape(8,8,8,8)
private val authorcorner= RoundedCornerShape(8,8,8,8)

@Composable
fun ChatScreen(navController: NavController){
    ChatScreenGenerate()
}
@Composable
fun ChatScreenGenerate(
                  ){

}
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ShowChat(){
    ChatScreenGenerate()
}