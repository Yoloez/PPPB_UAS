package com.example.pppb_uas.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    userName: String,
    userEmail: String,
    onCourseClick: () -> Unit,
    onLecturerClick: () -> Unit,
    onStudentClick: () -> Unit,
    onLogout: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Dashboard Admin",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            userName,
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF015023),
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(
                            Icons.Filled.ExitToApp,
                            contentDescription = "Logout",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF015023))
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("Course", color = Color.White, fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))
            DashboardCard(title = "Teknologi Rekayasa Perangkat Lunak", onClick = onCourseClick)
            Spacer(Modifier.height(16.dp))
            Text("Lecturer", color = Color.White, fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))
            DashboardCard(title = "Teknologi Rekayasa Perangkat Lunak", onClick = onLecturerClick)
            Spacer(Modifier.height(16.dp))
            Text("Student", color = Color.White, fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))
            DashboardCard(title = "Teknologi Rekayasa Perangkat Lunak", onClick = onStudentClick)
        }
    }

    // Logout Confirmation Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Konfirmasi Logout") },
            text = { Text("Apakah Anda yakin ingin keluar?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    }
                ) {
                    Text("Ya", color = Color(0xFF015023))
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Batal", color = Color.Gray)
                }
            }
        )
    }
}

@Composable
fun DashboardCard(title: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F0D8))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, color = Color.Black)
            Text("2024/2025", color = Color.Gray)
        }
    }
}