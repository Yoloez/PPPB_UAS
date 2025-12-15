package com.example.pppb_uas.ui.Course

import android.widget.Toast
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

// Definisi Font (Private)
private val urbanistFontFamily = FontFamily(
    Font(R.font.urbanist_regular, FontWeight.Normal),
    Font(R.font.urbanist_medium, FontWeight.Medium),
    Font(R.font.urbanist_semibold, FontWeight.SemiBold),
    Font(R.font.urbanist_bold, FontWeight.Bold)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCourseScreen(
    navController: NavController,
    viewModel: CourseViewModel = viewModel(),
    token: String,
    courseId: Int,
    initialName: String,
    initialCode: String,
    initialSks: Int
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf(initialName) }
    var code by remember { mutableStateOf(initialCode) }
    var sks by remember { mutableStateOf(initialSks.toString()) }

    var nameError by remember { mutableStateOf(false) }
    var codeError by remember { mutableStateOf(false) }
    var sksError by remember { mutableStateOf(false) }

    val darkGreen = Color(0xFF015023)
    val lightGreen = Color(0xFF015023)
    val yellowButton = Color(0xFFDABC4E)
    val errorColor = Color(0xFFFF5252)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Edit Course",
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = darkGreen)
            )
        },
        containerColor = darkGreen
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Input Nama Course
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
                    onValueChange = { name = it; nameError = it.isBlank() },
                    modifier = Modifier.fillMaxWidth(),
                    isError = nameError,
                    textStyle = TextStyle(fontFamily = urbanistFontFamily, fontSize = 16.sp, color = Color.White),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = lightGreen,
                        unfocusedContainerColor = lightGreen,
                        focusedBorderColor = if (nameError) errorColor else Color.White,
                        unfocusedBorderColor = if (nameError) errorColor else Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White,
                        errorBorderColor = errorColor
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                if (nameError) {
                    Text(
                        text = "Nama course tidak boleh kosong",
                        color = errorColor,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                        fontFamily = urbanistFontFamily
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Input ID (Code Subject)
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "ID:",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontFamily = urbanistFontFamily
                )

                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it; codeError = it.isBlank() },
                    modifier = Modifier.fillMaxWidth(),
                    isError = codeError,
                    textStyle = TextStyle(fontFamily = urbanistFontFamily, fontSize = 16.sp, color = Color.White),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = lightGreen,
                        unfocusedContainerColor = lightGreen,
                        focusedBorderColor = if (codeError) errorColor else Color.White,
                        unfocusedBorderColor = if (codeError) errorColor else Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White,
                        errorBorderColor = errorColor
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                if (codeError) {
                    Text(
                        text = "ID tidak boleh kosong",
                        color = errorColor,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                        fontFamily = urbanistFontFamily
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Input SKS
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
                    onValueChange = {
                        if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                            sks = it
                            sksError = it.isBlank()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isError = sksError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(fontFamily = urbanistFontFamily, fontSize = 16.sp, color = Color.White),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = lightGreen,
                        unfocusedContainerColor = lightGreen,
                        focusedBorderColor = if (sksError) errorColor else Color.White,
                        unfocusedBorderColor = if (sksError) errorColor else Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White,
                        errorBorderColor = errorColor
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                if (sksError) {
                    Text(
                        text = "SKS tidak boleh kosong",
                        color = errorColor,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                        fontFamily = urbanistFontFamily
                    )
                }
            }

            Spacer(modifier = Modifier.height(50.dp))

            // Tombol Update
            Button(
                onClick = {
                    nameError = name.isBlank()
                    codeError = code.isBlank()
                    sksError = sks.isBlank()
                    if (!nameError && !codeError && !sksError) {
                        viewModel.updateCourse(token, courseId, name, code, sks) {
                            Toast.makeText(context, "Course berhasil diupdate", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    } else {
                        Toast.makeText(context, "Mohon lengkapi semua field", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = !viewModel.isLoading.value,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = yellowButton,
                    disabledContainerColor = yellowButton.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                if (viewModel.isLoading.value) {
                    CircularProgressIndicator(color = darkGreen, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = "Update",
                        color = darkGreen,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = urbanistFontFamily
                    )
                }
            }

            if (viewModel.errorMessage.value.isNotEmpty() && !viewModel.isLoading.value) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = viewModel.errorMessage.value,
                    color = errorColor,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontFamily = urbanistFontFamily
                )
            }
        }
    }
}