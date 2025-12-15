package com.example.pppb_uas.ui.Course

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pppb_uas.R
import com.example.pppb_uas.viewmodel.CourseViewModel

// Definisi Font (Private agar tidak bentrok dengan file lain)
private val urbanistFontFamily = FontFamily(
    Font(R.font.urbanist_regular, FontWeight.Normal),
    Font(R.font.urbanist_medium, FontWeight.Medium),
    Font(R.font.urbanist_semibold, FontWeight.SemiBold),
    Font(R.font.urbanist_bold, FontWeight.Bold)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCourseScreen(
    navController: NavController,
    viewModel: CourseViewModel = viewModel(),
    token: String
) {
    val context = LocalContext.current
    val errorMessage by viewModel.errorMessage

    LaunchedEffect(errorMessage) {
        if (errorMessage.isNotEmpty()) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    var name by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var sks by remember { mutableStateOf("") }

    val darkGreen = Color(0xFF015023)
    val lightGreen = Color(0xFF015023)
    val yellowButton = Color(0xFFDABC4E)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Add Course",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontFamily = urbanistFontFamily
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF015023))
            )
        },
        containerColor = Color(0xFF015023)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // --- INPUT 1: COURSE NAME ---
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Course Name",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontFamily = urbanistFontFamily
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontFamily = urbanistFontFamily, fontSize = 16.sp, color = Color.White),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = lightGreen,
                        unfocusedContainerColor = lightGreen,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- INPUT 2: CODE ---
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Code (Kode Matkul):",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontFamily = urbanistFontFamily
                )

                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it },
                    modifier = Modifier.fillMaxWidth().background(darkGreen),
                    textStyle = TextStyle(fontFamily = urbanistFontFamily, fontSize = 16.sp, color = Color.White),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = lightGreen,
                        unfocusedContainerColor = lightGreen,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- INPUT 3: SKS ---
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "SKS:",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontFamily = urbanistFontFamily
                )

                OutlinedTextField(
                    value = sks,
                    onValueChange = { sks = it },
                    modifier = Modifier.fillMaxWidth().background(darkGreen),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(fontFamily = urbanistFontFamily, fontSize = 16.sp, color = Color.White),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = lightGreen,
                        unfocusedContainerColor = lightGreen,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            // --- SAVE BUTTON ---
            Button(
                onClick = {
                    if (name.isNotEmpty() && code.isNotEmpty() && sks.isNotEmpty()) {
                        viewModel.addCourse(token, name, code, sks) {
                            navController.popBackStack()
                        }
                    }
                },
                enabled = !viewModel.isLoading.value,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = yellowButton),
                shape = RoundedCornerShape(28.dp)
            ) {
                if (viewModel.isLoading.value) {
                    CircularProgressIndicator(color = darkGreen, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = "Save",
                        color = darkGreen,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = urbanistFontFamily
                    )
                }
            }
        }
    }
}