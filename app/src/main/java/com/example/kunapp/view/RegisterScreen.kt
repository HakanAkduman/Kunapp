package com.example.kunapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kunapp.R

@Composable
fun RegisterScreen(navController: NavController){
    ScreenRegisterGenerate(navController)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenRegisterGenerate(navController: NavController){

    var email by remember { mutableStateOf("") }
    var password1 by remember { mutableStateOf("") }
    var nick by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = nick,
            onValueChange = { nick = it },
            label = { Text(text = "Nick") },
            modifier = Modifier.fillMaxWidth()
        )



        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "E-posta") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))


        OutlinedTextField(
            value = password1,
            onValueChange = { password1 = it },
            label = { Text(text = "Şifre") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password2,
            onValueChange = { password2 = it },
            label = { Text(text = "Şifreyi tekrar giriniz") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )





        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("login_screen")

                //kayıt fonksiyonu yazılacak
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(50.dp)
        ) {
            Text(text = "Kayıt Ol")
        }

        Spacer(modifier = Modifier.height(16.dp))


    }
}
@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
    ScreenRegisterGenerate(NavController(LocalContext.current))
}