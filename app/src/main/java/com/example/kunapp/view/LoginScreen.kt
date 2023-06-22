package com.example.kunapp.view



import android.widget.Toast
import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.navigation.NavController
import com.example.kunapp.R
import com.example.kunapp.ui.theme.Typography
import com.example.kunapp.viewmodel.LoginScreenViewModel
import com.example.kunapp.viewmodel.RegisterScreenViewModel
import java.util.Locale


@Composable
fun LoginScreen(navController:NavController){

    LoginScreenGenerate(navController)


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginScreenGenerate(navController:NavController,viewModel: LoginScreenViewModel = remember { LoginScreenViewModel() }){
    val isLoading by viewModel.isLoading.observeAsState(false)
    val isSuccess by viewModel.isSuccess.observeAsState("")
    val isError by viewModel.isError.observeAsState("")


    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var emailEmpty by remember{ mutableStateOf(false) }
    var passwordEmpty by remember{ mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
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
        if (!isSuccess.isNullOrBlank()){
            navController.navigate("main_screen/$isSuccess") {
                launchSingleTop = true
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        Text(text = "Log in",
            style = Typography.titleLarge,
         modifier = Modifier.padding(5.dp).padding(vertical = 5.dp).fillMaxWidth())






        OutlinedTextField(
            value = email,
            onValueChange = {
                if(it.isEmpty()){
                    email = it
                }
                else if(!it[it.length-1].isWhitespace()) {
                    email = it.lowercase(Locale.ENGLISH)
                } },
            label = { Text(text = "E-posta") },
            modifier = Modifier.fillMaxWidth(),
            isError = emailEmpty, maxLines = 1,
            supportingText = {
                if (emailEmpty) {
                    Text(
                        text = "Doldurulması zorunlu alan",
                        style = MaterialTheme.typography.titleSmall.copy(fontSize = 14.sp)
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = password,
                onValueChange = {
                    if(it.isEmpty()){
                        password = it
                    }
                    else if(!it[it.length-1].isWhitespace()) {
                        password = it
                    } },
                label = { Text(text = "Şifre") },
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(), maxLines = 1,
                isError = passwordEmpty,
                supportingText = {
                    if (passwordEmpty) {
                        Text(
                            text = "Doldurulması zorunlu alan",
                            style = MaterialTheme.typography.titleSmall.copy(fontSize = 14.sp)
                        )
                    }
                }
            )



            IconButton(
                onClick = {
                    passwordVisibility = !passwordVisibility
                },

                modifier = Modifier
                    .size(35.dp)
                    .align(Alignment.CenterEnd)
                    .padding(end = 10.dp, top = 5.dp)
            ) {
                Icon(
                    painter = painterResource(id = if(passwordVisibility) R.drawable.show else R.drawable.eye),
                    contentDescription = if (passwordVisibility) "Şifreyi Gizle" else "Şifreyi Göster",
                    tint = Color.Gray, modifier = Modifier.size(60.dp)
                )
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                emailEmpty=false
                passwordEmpty=false

                if(email.isNullOrEmpty()){
                    emailEmpty=true
                }
                if(password.isNullOrEmpty()){
                    passwordEmpty=true
                }
                if(!emailEmpty&&!passwordEmpty){
                    viewModel.Login(email, password = password, navController = navController)
                }



            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp, start = 50.dp, end = 50.dp)
        ) {
            Text(text = "Giriş Yap")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {

                navController.navigate("register_screen")

            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 50.dp, end = 50.dp)
        ) {
            Text(text = "Kayıt Ol")
        }


    }




}
@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    LoginScreenGenerate(navController = NavController(LocalContext.current))
}