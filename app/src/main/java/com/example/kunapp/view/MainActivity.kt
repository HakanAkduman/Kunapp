package com.example.kunapp.view

import android.widget.ImageButton
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavArgs
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import androidx.navigation.navArgument

import com.example.kunapp.R

import androidx.navigation.navArgument

@Composable
fun MainScreen(nick: String?,navController: NavController){

    MainScreenGenerate(nick = nick, navController = navController)


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenGenerate(nick:String?,navController: NavController){

    val mainNavController = rememberNavController()

    var clickedIndex by remember { mutableStateOf(1) }

    Scaffold(
        bottomBar = {
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    onClick = {
                        mainNavController.navigate("new_post_screen/$nick")
                        clickedIndex = 0
                    },
                    colors = if (clickedIndex == 0) ButtonDefaults.buttonColors(Color.Red) else ButtonDefaults.buttonColors(
                        Color.Cyan
                    )
                ) {
                    ImageButton(
                        onClick = {
                            mainNavController.navigate("new_post_screen/$nick")
                            clickedIndex = 0
                        },
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = R.drawable.edit)
                    )
                }

                Button(
                    onClick = {
                        mainNavController.navigate("post_screen/$nick")
                        clickedIndex = 1
                    },
                    colors = if (clickedIndex == 1) ButtonDefaults.buttonColors(Color.Red) else ButtonDefaults.buttonColors(
                        Color.Cyan
                    )
                ) {
                    ImageButton(
                        onClick = {
                            mainNavController.navigate("post_screen/$nick")
                            clickedIndex = 1
                        },
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = R.drawable.home)
                    )
                }

                Button(
                    onClick = {
                        mainNavController.navigate("profile_screen/$nick/$nick")
                        clickedIndex = 2
                    },
                    colors = if (clickedIndex == 2) ButtonDefaults.buttonColors(Color.Red) else ButtonDefaults.buttonColors(
                        Color.Cyan
                    )
                ) {
                    ImageButton(
                        onClick = {
                            mainNavController.navigate("profile_screen/$nick/$nick")
                            clickedIndex = 2
                        },
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = R.drawable.user)
                    )
                }
            }
        }

    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            NavHost(navController = mainNavController, startDestination = "post_screen/$nick") {
                composable(
                    route = "post_screen/{nick}",
                    arguments = listOf(navArgument("nick") { type = NavType.StringType })
                ) { backStackEntry ->
                    val routeNick = backStackEntry.arguments?.getString("nick")
                    val postNick = if (routeNick.isNullOrEmpty()) nick else routeNick
                    PostScreen(nick = postNick, navController = mainNavController, loginNavController = navController)
                }

                composable(
                    route = "profile_screen/{userNick}/{profileNick}",
                    arguments = listOf(
                        navArgument("userNick") { type = NavType.StringType },
                        navArgument("profileNick") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val userNick = backStackEntry.arguments?.getString("userNick")
                    val profileNick = backStackEntry.arguments?.getString("profileNick")
                    ProfileScreen(
                        userNick = userNick,
                        profileNick = profileNick,
                        navController = mainNavController, loginNavController = navController
                    )
                }

                composable(
                    route = "new_post_screen/{nick}",
                    arguments = listOf(navArgument("nick") { type = NavType.StringType })
                ) { backStackEntry ->
                    val newPostNick = backStackEntry.arguments?.getString("nick")
                    NewPostScreen(nick = newPostNick, navController = mainNavController)
                }
                composable("chat_screen/{userNick}/{messageID}", arguments = listOf(
                    navArgument("userNick"){
                        type= NavType.StringType
                    } ,
                    navArgument("messageID"){
                        type= NavType.StringType
                    }
                )){
                        backStackEntry ->
                    val userNick = backStackEntry.arguments?.getString("userNick")

                    val messageID = backStackEntry.arguments?.getString("messageID")
                    ChatScreen(navController = navController, id = messageID, userNick = userNick!!)}
                composable("message_screen/{userNick}", arguments = listOf(
                    navArgument("userNick"){
                        type= NavType.StringType
                    }
                )){ backStackEntry ->
                    val userNick = backStackEntry.arguments?.getString("userNick")
                    MessagesScreen(navController = navController, userNick = userNick!!)

                }
            }
        }
    }

}




@Preview(showBackground = true)
@Composable
private fun ScreenPreview() {
    MainScreenGenerate("",navController = NavController(LocalContext.current))
}