package com.example.kunapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController

@Composable
fun PostScreen(nick:String?,navController: NavController){

    PostScreenGenerate(navController)


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PostScreenGenerate(navController: NavController){
    Column(modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Button(onClick = { /*TODO*/ }) {
                Text(text = "popüler")
            }
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Takipler")
            }

        }

    }

}
@Preview(showBackground = true)
@Composable
private fun ScreenPreview() {
    PostScreenGenerate(navController = NavController(LocalContext.current))
}