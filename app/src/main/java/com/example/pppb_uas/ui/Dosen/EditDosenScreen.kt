package com.example.pppb_uas.ui.Dosen
import androidx.compose.ui.text.input.PasswordVisualTransformation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pppb_uas.model.Dosen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDosenScreen(
    existingDosen: Dosen,
    onBackClick: () -> Unit = {},
    onUpdateClick: (Dosen) -> Unit = {}
) {
    var dosenData by remember { mutableStateOf(existingDosen) }

    val darkGreen = Color(0xFF015023)
    val lightGreen = Color(0xFF015023)
    val yellowButton = Color(0xFFDABC4E)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkGreen)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Edit Lecture",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Avatar (edit)
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Surface(
                    modifier = Modifier.size(100.dp),
                    shape = CircleShape,
                    color = lightGreen,
                    onClick = {
                        // TODO: Image picker
                    }
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(100.dp)
                            .border(2.dp, Color.Black, shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Edit Photo",
                            tint = Color.Black.copy(alpha = 0.7f),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Change Foto",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Name
            Column {
                Text(
                    text = "Name:",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = dosenData.name,
                    onValueChange = { dosenData = dosenData.copy(name = it) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = lightGreen,
                        unfocusedContainerColor = lightGreen,
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // NIP
            Column {
                Text(
                    text = "NIP:",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = dosenData.nip,
                    onValueChange = { dosenData = dosenData.copy(nip = it) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = lightGreen,
                        unfocusedContainerColor = lightGreen,
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Email
            Column {
                Text(
                    text = "Email:",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = dosenData.email,
                    onValueChange = { dosenData = dosenData.copy(email = it) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = lightGreen,
                        unfocusedContainerColor = lightGreen,
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Password (optional)
            Column {
                Text(
                    text = "Password:",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = dosenData.password,
                    onValueChange = { dosenData = dosenData.copy(password = it) },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = lightGreen,
                        unfocusedContainerColor = lightGreen,
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // UPDATE BUTTON
            Button(
                onClick = { onUpdateClick(dosenData) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = yellowButton),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "Update",
                    color = darkGreen,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditDosenPreview() {
    EditDosenScreen(
        existingDosen = Dosen(
            id = "D001",
            name = "Dr. Ahmad",
            nip = "123456",
            email = "ahmad@example.com",
            password = "secret123",
            photoUrl = ""
        )
    )
}
