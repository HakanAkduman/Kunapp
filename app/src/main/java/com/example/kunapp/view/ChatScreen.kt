package com.example.kunapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kunapp.R
import com.example.kunapp.model.Comment
import com.example.kunapp.model.MesageRow
import com.example.kunapp.model.Message
import com.example.kunapp.viewmodel.ChatScreenViewModel


@Composable
fun ChatScreen(
    id: String?,
    navController: NavController,
    userNick: String,
    viewModel: ChatScreenViewModel = remember { ChatScreenViewModel() }
) {
    id.let {
        viewModel.openMessages(id!!)
    }

    val isSuccess by viewModel.isSuccess.observeAsState(emptyList())
    val messageList = remember { mutableStateListOf<Message>() }
    if(messageList.isEmpty()){
        messageList.addAll(isSuccess)
    }


    ChatScreenGenerate(
        userNick = userNick,
        viewModel = viewModel,
        id = id,
        messageList = messageList
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreenGenerate(
    userNick: String,
    viewModel: ChatScreenViewModel,
    id: String?,
    messageList: MutableList<Message>
) {
    var sendMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                modifier = Modifier.fillMaxHeight(),
                reverseLayout = true,
                
            ) {
                items(items = messageList.reversed()) { message ->
                    MessageBox(
                        message = message,
                        userNick = userNick,
                        showSenderName = true
                    )
                }
            }
        }

        Row(modifier = Modifier.padding(bottom = 5.dp)) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = sendMessage,
                onValueChange = { sendMessage = it },
                placeholder = { Text(text = "message...") },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Gray,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                shape = CircleShape
            )
            Spacer(modifier = Modifier.width(8.dp))
            ImageButton(
                modifier = Modifier.size(50.dp),
                painter = painterResource(id = R.drawable.send_m)
            ) {
                if (sendMessage.isNotBlank()) {
                    viewModel.sendMessage(userNick, sendMessage, id!!)
                    messageList.add(Message(userNick, sendMessage))
                    sendMessage = ""
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageBox(message: Message, userNick: String, showSenderName: Boolean) {
    val isUserMessage = message.sender == userNick

    Row(
        horizontalArrangement = if (isUserMessage) Arrangement.End else Arrangement.Start,
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
    ) {
        if (showSenderName) {
            Text(
                text = message.sender + " :",
                fontWeight = FontWeight.Bold,
                color = if (isUserMessage) Color.Blue else Color.Black
            )
            Spacer(modifier = Modifier.padding(4.dp))
        } else {
            Spacer(modifier = Modifier.width(60.dp))
        }

        Text(
            text = message.message,
            color = if (isUserMessage) Color.DarkGray else Color.Black
        )
    }
}





@Preview(showSystemUi = true)
@Composable
fun ShowChat() {

}