package com.example.kunapp.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.kunapp.MainActivity
import com.example.kunapp.R
import com.example.kunapp.viewmodel.NewPostScreenViewModel


@Composable
fun NewPostScreen(navController: NavController) {

    NewPostScreenGenerate(navController)


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewPostScreenGenerate(
    navController: NavController,
    viewModel: NewPostScreenViewModel = remember { NewPostScreenViewModel() }
) {
    var postText by remember { mutableStateOf("") }
    var photoUri: Uri? by remember { mutableStateOf(null) }


    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.80F)
            .padding(vertical = 10.dp, horizontal = 15.dp)
    ) {
        OutlinedTextField(
            value = postText,
            onValueChange = { postText = it },
            label = { Text(text = "Paylaşmak istediklerinizi girebilirsiniz") },
            modifier = Modifier.fillMaxWidth()
        )

        ImagePicker() {
            photoUri = it
        }
        Button(onClick = { viewModel.share(postText, photoUri = photoUri) }) {
            Text(text = "Paylaş")
        }

    }

}

@Composable
fun ImagePicker(onImageSelected: (Uri) -> Unit) {
    val context = LocalContext.current
    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val imageUri = result.data?.data
                if (imageUri != null) {
                    selectedImageUri.value = imageUri
                    onImageSelected(imageUri)
                }
            }
        }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    launcher.launch(intent)
                } else {
                    ActivityCompat.requestPermissions(
                        MainActivity(),
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        1
                    )
                }
            },
        contentAlignment = Alignment.Center
    ) {
        if (selectedImageUri.value != null) {
            val imagePainter: Painter = rememberAsyncImagePainter(selectedImageUri!!)
            Image(
                painter = imagePainter,
                contentDescription = "Selected Image",
                modifier = Modifier.fillMaxSize()
            )
        } else {
            val imagePainter: Painter = painterResource(R.drawable.tap_to_load_image)
            Image(
                painter = imagePainter,
                contentDescription = "Selected Image",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}


@Composable
fun ImageButton(
    modifier: Modifier,
    drawableToDraw: Int,
    description: String? = null,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        color = Color.Transparent
    ) {
        Image(
            painter = painterResource(drawableToDraw),
            contentDescription = description,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenPreview() {
    NewPostScreenGenerate(navController = NavController(LocalContext.current))
}