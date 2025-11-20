package com.example.pppb_uas

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("dashboard") {
            DashboardScreen(
                onCourseClick = { navController.navigate("manage_course") },
                onLecturerClick = { navController.navigate("manage_lecturer") },
                onStudentClick = { navController.navigate("manage_student") }
            )
        }
    }
}

@Composable
fun DashboardScreen(
    onCourseClick: () -> Unit,
    onLecturerClick: () -> Unit,
    onStudentClick: () -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardPreview() {
    MaterialTheme {
        DashboardScreen(
            onCourseClick = {},
            onLecturerClick = {},
            onStudentClick = {}
        )
    }
}

