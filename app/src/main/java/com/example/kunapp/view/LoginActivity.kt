package com.example.kunapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.NavigatorProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kunapp.ui.theme.KunappTheme
import com.example.kunapp.view.LoginScreen
import com.example.kunapp.view.MainScreen
import com.example.kunapp.view.RegisterScreen
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            KunappTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login_screen") {
                    composable("login_screen") { LoginScreen(navController = navController)}
                    composable("register_screen") { RegisterScreen(navController = navController) }
                    composable("main_screen/{nick}", arguments = listOf(
                        navArgument("nick"){
                            type=NavType.StringType
                        }
                    )) {
                        val nick=remember{
                            it.arguments?.getString("nick")
                        }
                        MainScreen(nick,navController = navController)
                    }

                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KunappTheme {

    }
}