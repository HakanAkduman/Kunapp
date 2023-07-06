package com.example.kunapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kunapp.R
import com.example.kunapp.model.MesageRow
import com.example.kunapp.viewmodel.ChatScreenViewModel


@Composable
fun ChatScreen(id:String,navController: NavController,userNick:String,viewModel: ChatScreenViewModel=remember{ ChatScreenViewModel() }) {
    viewModel.open()
    ChatScreenGenerate(userNick = userNick, viewModel = viewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreenGenerate(userNick:String,viewModel: ChatScreenViewModel
) {



    Column(
        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn() {
            items(items=listOf<MesageRow>()){
                MesageBox(message = it, userNick = )
          }
        }
        Text_Field()
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MesageBox(message:MesageRow,userNick: String) {

    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(id = R.drawable.user),
            contentDescription = "profile",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(15.dp, Color(color = 2), CircleShape)

        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier) {
            Surface(
                modifier = Modifier.background(color = LightGray),
                shape = MaterialTheme.shapes.large,
            ) {
                Text(
                    modifier = Modifier
                        .padding(vertical = 7.dp)
                        .padding(5.dp),
                    text = "hello dear how are u",
                    style = MaterialTheme.typography.bodyLarge
                )

            }


        }
    }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Text_Field() {
    var send_message by remember { mutableStateOf("") }
    Row(modifier = Modifier.padding(bottom = 5.dp)) {
        TextField(
            modifier = Modifier.fillMaxWidth(0.85f),
            value = send_message, onValueChange = { (it) },
            placeholder = {
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = "Selam Naber",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.Black
                    ),
                    textAlign = TextAlign.Center
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Gray,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),

            shape = CircleShape
        )
        ImageButton(
            modifier = Modifier
                .size(50.dp)
                .padding(top = 5.dp), drawableToDraw = R.drawable.send_m
        ) {

        }
    }


}




@Preview(showSystemUi = true)
@Composable
fun ShowChat() {

}