package com.example.pppb_uas.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pppb_uas.R
import com.example.pppb_uas.ui.theme.creamColor
import com.example.pppb_uas.ui.theme.goldAccent
import com.example.pppb_uas.ui.theme.lightGreen
import com.example.pppb_uas.ui.theme.primary
import com.example.pppb_uas.ui.theme.primaryGreen

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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Avatar/Initial User
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(goldAccent),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = userName.firstOrNull()?.uppercaseChar()?.toString() ?: "A",
                                color = primaryGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                "Dashboard Admin",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.White
                            )
                            Text(
                                userName,
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryGreen
                ),
                actions = {
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(
                            Icons.Filled.ExitToApp,
                            contentDescription = "Logout",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                        primaryGreen)

                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Welcome Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = creamColor)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.School,
                            contentDescription = null,
                            tint = primaryGreen,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text(
                                "Selamat Datang",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                userName,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryGreen
                            )
                            Text(
                                userEmail,
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Spacer(Modifier.height(28.dp))

                // Section Title
                Text(
                    "Kelola Data Akademik",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Menu Cards dengan Background Batik
                BatikMenuCard(
                    label = "Course",
                    title = "Manajemen Mata Kuliah",
                    subtitle = "Kelola data mata kuliah dan jadwal",
                    onClick = onCourseClick
                )

                Spacer(Modifier.height(16.dp))

                BatikMenuCard(
                    label = "Lecturer",
                    title = "Manajemen Dosen",
                    subtitle = "Kelola data dosen pengajar",
                    onClick = onLecturerClick
                )

                Spacer(Modifier.height(16.dp))

                BatikMenuCard(
                    label = "Student",
                    title = "Manajemen Mahasiswa",
                    subtitle = "Kelola data mahasiswa aktif",
                    onClick = onStudentClick
                )

                Spacer(Modifier.height(24.dp))

                // Footer Info
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.1f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        InfoBadge(icon = Icons.Filled.DateRange, label = "Tahun Ajaran", value = "2024/2025")
                        Divider(
                            modifier = Modifier
                                .height(40.dp)
                                .width(1.dp),
                            color = Color.White.copy(alpha = 0.3f)
                        )
                        InfoBadge(icon = Icons.Filled.CalendarToday, label = "Semester", value = "Genap")
                    }
                }
            }
        }
    }

    // Logout Confirmation Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            icon = {
                Icon(
                    Icons.Filled.ExitToApp,
                    contentDescription = null,
                    tint = primaryGreen,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    "Konfirmasi Logout",
                    fontWeight = FontWeight.Bold,
                    color = primaryGreen
                )
            },
            text = {
                Text(
                    "Apakah Anda yakin ingin keluar dari sistem?",
                    color = Color.DarkGray
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = primaryGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Ya, Keluar",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showLogoutDialog = false },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Batal")
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
        )
    }
}

@Composable
fun BatikMenuCard(
    label: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .shadow(6.dp, RoundedCornerShape(20.dp))
            .clickable {
                android.util.Log.d("UI_DEBUG", "Card diklik: $title")
                onClick()
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Background Batik Image
            Image(
                painter = painterResource(id = R.drawable.batik),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Content Overlay
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Label Badge
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = goldAccent,
                    modifier = Modifier.wrapContentSize()
                ) {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp)
                    )
                }

                Spacer(Modifier.weight(1f))

                // Title and Subtitle
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C2C2C)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color(0xFF5C5C5C),
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
fun InfoBadge(icon: ImageVector, label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.9f),
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color.White.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}