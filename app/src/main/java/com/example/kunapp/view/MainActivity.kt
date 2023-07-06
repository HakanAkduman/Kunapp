package com.example.kunapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavArgs
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kunapp.R

@Composable
fun MainScreen(nick: String?,navController: NavController){

    MainScreenGenerate(nick = nick, navController = navController)


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenGenerate(nick:String?,navController: NavController){
    println(nick)

Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()) {
    val mainNavController = rememberNavController()
    var clickedIndex by remember{ mutableStateOf(1) }
    NavHost(navController = mainNavController, startDestination = "post_screen",
    ){
        composable("post_screen"){ PostScreen(navController = mainNavController)}
        composable("profile_screen"){ ProfileScreen(navController = mainNavController)}
        composable("new_post_screen"){ NewPostScreen(navController = mainNavController)}
        composable("chat_screen"){ ChatScreen(navController = navController)}
        composable("message_screen/{userNick}",NavArgs(
            navArgument("userNick"){
                            }
        )){
            MessagesScreen(navController = navController, userNick = nick!!)
        }
    }
    Row(verticalAlignment = Alignment.Bottom, modifier = Modifier
        .fillMaxWidth()
        .fillMaxWidth()
        , horizontalArrangement = Arrangement.SpaceAround) {
        Button(onClick = {  changeNavHost(mainNavController,"new_post_screen")
            clickedIndex=0
},
        colors =if (clickedIndex==0) ButtonDefaults.buttonColors(Color.Red) else ButtonDefaults.buttonColors(Color.Cyan)
        ) {

            ImageButton(
                onClick = {

                    changeNavHost(mainNavController,"new_post_screen")
                    clickedIndex=0},
                modifier = Modifier.size(20.dp),
                drawableToDraw =R.drawable.edit )
        }

        Button(onClick = {
            changeNavHost(mainNavController,"post_screen")
            clickedIndex=1
           },
            colors =if (clickedIndex==1) ButtonDefaults.buttonColors(Color.Red) else ButtonDefaults.buttonColors(Color.Cyan))
        {

            ImageButton(
                onClick = {
                    changeNavHost(mainNavController,"post_screen")
                    clickedIndex=1
                },
                modifier = Modifier.size(20.dp),
                drawableToDraw = R.drawable.home)
                

        }
        Button(onClick = {changeNavHost(mainNavController,"profile_screen")
            clickedIndex=2
           },
            colors =if (clickedIndex==2) ButtonDefaults.buttonColors(Color.Red) else ButtonDefaults.buttonColors(Color.Cyan))
        {

            ImageButton(
                onClick = {
                    changeNavHost(mainNavController,"profile_screen")
                    clickedIndex=2
                },
                modifier = Modifier.size(20.dp),
                drawableToDraw = R.drawable.user )
                

        }
    }
}

}
fun changeNavHost(navController:NavController,str:String){
    navController.navigate(str)
}



@Preview(showBackground = true)
@Composable
private fun ScreenPreview() {
    MainScreenGenerate("",navController = NavController(LocalContext.current))
}