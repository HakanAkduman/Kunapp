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
    MessageScreenGenerate(userNick=userNick, viewModel = viewModel)
}

@Composable
fun MessageScreenGenerate(userNick:String,viewModel: MessageScreenViewModel

) {
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
                UserEachRow(mesageRow = it, userNick = userNick)
            }
        }

    }
}

@Composable
fun UserEachRow(mesageRow: MesageRow,userNick: String// chat screen e gidecek navConcontroller
) {

Row (modifier = Modifier
    .padding(vertical = 1.dp)
    .padding(start = 2.dp)
    .clickable {
        navController.navigate("chat_screen/${mesageRow.id}")
    }
){
    Image(painter = rememberAsyncImagePainter(model = mesageRow.profileUrl), contentDescription ="person" , modifier = Modifier
        .size(55.dp)
        .clip(CircleShape)
        .border(15.dp, Color(color = 2), CircleShape))
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
        Text(text = mesageRow.list.get(mesageRow.list.size-1).get("message")!!, modifier = Modifier
            .padding(vertical = 2.dp)
            .padding(start = 4.dp), fontSize = 20.sp)
    }
    Spacer(modifier = Modifier.padding(end = 100.dp))
    Text(text = mesageRow.list.get(mesageRow.list.size-1).get("date")!!, modifier = Modifier)




}

}

@Preview(showSystemUi = true)
@Composable
fun MessageScreen() {
   MessageScreenGenerate("", MessageScreenViewModel())
}

