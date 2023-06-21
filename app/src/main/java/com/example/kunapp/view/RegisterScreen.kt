package com.example.kunapp.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.size.Size
import com.example.kunapp.viewmodel.RegisterScreenViewModel
import okio.utf8Size
import org.intellij.lang.annotations.Language
import java.util.Locale

@Composable
fun RegisterScreen(navController: NavController) {
    ScreenRegisterGenerate(navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenRegisterGenerate(navController: NavController, viewModel: RegisterScreenViewModel = remember { RegisterScreenViewModel() })
{

    val isLoading by viewModel.isLoading.observeAsState(false)
    val isSuccess by viewModel.isSuccess.observeAsState(false)
    val isError by viewModel.isError.observeAsState("")

    var email by remember { mutableStateOf("") }
    var password1 by remember { mutableStateOf("") }
    var nick by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }
    var emailEmpty by remember { mutableStateOf(false) }
    var password1Empty by remember { mutableStateOf(false) }
    var password2Empty by remember { mutableStateOf(false) }
    var nickEmpty by remember { mutableStateOf(false) }
    var passwordsEqual by remember { mutableStateOf(true) }

    Box(modifier = Modifier.fillMaxSize()) {
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
        if (isError.isNotEmpty()) {
            Toast.makeText(LocalContext.current, isError, Toast.LENGTH_LONG).show()

        }
        if (isSuccess){
            navController.navigate("login_screen"){
                launchSingleTop = true
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {




            OutlinedTextField(
                value = nick,
                onValueChange = {
                    if(it.isEmpty()){
                        nick = it
                    }
                    else if(!it[it.length-1].isWhitespace()) {
                    nick = it.lowercase(Locale.ENGLISH)
                }
                }, maxLines = 1,
                label = { Text(text = "Nick") },
                modifier = Modifier.fillMaxWidth(),
                isError = nickEmpty,
                supportingText = {
                    if (nickEmpty) {
                        Text(
                            text = "Doldurulması zorunlu alan",
                            style = MaterialTheme.typography.titleSmall.copy(fontSize = 14.sp)
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

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

            OutlinedTextField(
                value = password1,
                onValueChange = {
                    if(it.isEmpty()){
                    password1 = it
                }
                else if(!it[it.length-1].isWhitespace()) {
                    password1 = it
                } },
                label = { Text(text = "Şifre") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(), maxLines = 1,
                isError = password1Empty || !passwordsEqual,
                supportingText = {
                    if (password1Empty) {
                        Text(
                            text = "Doldurulması zorunlu alan",
                            style = MaterialTheme.typography.titleSmall.copy(fontSize = 14.sp)
                        )
                    } else if (!passwordsEqual) {
                        Text(
                            text = "Şifreler aynı olmak zorundadır",
                            style = MaterialTheme.typography.titleSmall.copy(fontSize = 14.sp)
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password2,
                onValueChange = {
                    if(it.isEmpty()){
                        password2 = it
                    }
                    else if(!it[it.length-1].isWhitespace()) {
                    password2 = it
                } },
                label = { Text(text = "Şifreyi tekrar giriniz") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(), maxLines = 1,
                isError = password2Empty || !passwordsEqual,
                supportingText = {
                    if (password2Empty) {
                        Text(
                            text = "Doldurulması zorunlu alan",
                            style = MaterialTheme.typography.titleSmall.copy(fontSize = 14.sp)
                        )
                    } else if (!passwordsEqual) {
                        Text(
                            text = "Şifreler aynı olmak zorundadır",
                            style = MaterialTheme.typography.titleSmall.copy(fontSize = 14.sp)
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {

                    passwordsEqual = true
                    password2Empty = false
                    password1Empty = false
                    emailEmpty = false
                    nickEmpty = false

                    if (password1.isNullOrEmpty()) {
                        password1Empty = true
                    }
                    if (password2.isNullOrEmpty()) {
                        password2Empty = true
                    }
                    if (nick.isNullOrEmpty()) {
                        nickEmpty = true
                    }
                    if (email.isNullOrEmpty()) {
                        emailEmpty = true
                    }
                    if (!(password1.isNullOrEmpty() || password2.isNullOrEmpty()) && password1 != password2) {
                        passwordsEqual = false
                    }

                    if (passwordsEqual && !password1Empty && !password2Empty && !nickEmpty && !emailEmpty) {
                        viewModel.register(email, password1, nick)
                    }
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
}

@Preview(showBackground = true)
@Composable
private fun ScreenPreview() {
    ScreenRegisterGenerate(NavController(LocalContext.current))
}
