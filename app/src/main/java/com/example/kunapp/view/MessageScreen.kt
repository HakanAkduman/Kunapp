package com.example.kunapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.kunapp.R
import com.example.kunapp.model.MesageRow
import com.example.kunapp.viewmodel.MessageScreenViewModel


@Composable
fun MessagesScreen(navController: NavController,userNick:String,viewModel: MessageScreenViewModel = remember{ MessageScreenViewModel() }) {
    viewModel.loadMessages(userNick)
    MessageScreenGenerate(navController = navController,userNick=userNick, viewModel = viewModel)
}

@Composable
fun MessageScreenGenerate(navController: NavController,userNick:String,viewModel: MessageScreenViewModel

) {
    println("screen generated")
    val isSucces=viewModel.isSuccess.observeAsState(listOf<MesageRow>())
    val isLoading=viewModel.isLoading.observeAsState()
    val isError= viewModel.isError.observeAsState()

    Box(
        modifier = Modifier
            .background(
                Color.Gray,

                )
            .width(90.dp)
            .height(5.dp)
            .padding(top = 30.dp)

    ) {
        LazyColumn{

           items(items = isSucces.value){
                UserEachRow(mesageRow = it, userNick = userNick, navController = navController)
            }
        }

    }
}

@Composable
fun UserEachRow(mesageRow: MesageRow,userNick: String,navController: NavController
) {
println("user each row has worked")
Row (modifier = Modifier
    .padding(vertical = 10.dp)
    .padding(start = 2.dp)
    .clickable {
        navController.navigate("chat_screen/$userNick/${mesageRow.id}")
    }
){

    Column(){
        Text(
            text = when(userNick){
                mesageRow.user1->mesageRow.user2!!
                mesageRow.user2->mesageRow.user1!!
                else->mesageRow.user1!!
                  }
            , modifier = Modifier
                .padding(vertical = 1.dp)
                .padding(start = 4.dp), fontSize = 25.sp

        )
        Text(text = mesageRow.lastMessage, modifier = Modifier
            .padding(vertical = 2.dp)
            .padding(start = 4.dp), fontSize = 20.sp)
    }
    Spacer(modifier = Modifier.padding(end = 100.dp))
    Text(text = mesageRow.date, modifier = Modifier)




}

}

@Preview(showSystemUi = true)
@Composable
fun MessageScreen() {
   MessageScreenGenerate(userNick = "", viewModel = MessageScreenViewModel(), navController = NavController(
       LocalContext.current)
   )
}

