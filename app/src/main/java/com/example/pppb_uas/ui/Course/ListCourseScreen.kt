package com.example.pppb_uas.ui.Course

import com.example.pppb_uas.model.Course

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseListScreen(
    onBackClick: () -> Unit = {},
    onAddClick: () -> Unit = {},
    onEditClick: (Course) -> Unit = {},
    onDeleteClick: (Course) -> Unit = {}
) {
    // Sample data disesuaikan dengan Model Course (id, course, semester)
    val courses = remember {
        mutableStateListOf(
            Course(id = "1", course = "Pemrograman Mobile", semester = "5"),
            Course(id = "2", course = "Pengembangan Web", semester = "3"),
            Course(id = "3", course = "Kecerdasan Buatan", semester = "5"),
            Course(id = "4", course = "Basis Data Lanjut", semester = "4"),
            Course(id = "4", course = "Basis Data Lanjut", semester = "4")
        )
    }

    // Warna tema
    val darkGreen = Color(0xFF015023)
    val creamColor = Color(0xFFEFE7D3)
    val yellowColor = Color(0xFFDABC4E)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Course",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onAddClick) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Course",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = darkGreen
                )
            )
        },
        containerColor = darkGreen
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 20.dp)
        ) {
            itemsIndexed(courses) { index, course ->
                CourseCard(
                    course = course,
                    isSelected = index == 1, // Logic highlight (opsional)
                    backgroundColor = if (index == 1) yellowColor else creamColor,
                    onEditClick = { onEditClick(course) },
                    onDeleteClick = { onDeleteClick(course) }
                )
            }
        }
    }
}

@Composable
fun CourseCard(
    course: Course,
    isSelected: Boolean = false,
    backgroundColor: Color,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val darkGreen = Color(0xFF0D5C2F)
    val textColor = if (isSelected) Color.White else darkGreen

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Course Icon (Menggunakan DateRange sebagai simbol jadwal/kuliah)
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange, // Icon diganti agar sesuai konteks
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Nama Matkul dan Semester
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = course.course, // Nama Mata Kuliah
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textColor,
                    maxLines = 1 // Biar tidak terlalu panjang jika nama matkul panjang
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Semester ${course.semester}", // Menampilkan Semester
                    fontSize = 14.sp,
                    color = textColor.copy(alpha = 0.8f)
                )
            }

            // Action Buttons (Edit & Delete)
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = textColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = textColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

// Preview
@Preview(showBackground = true)
@Composable
fun CourseListScreenPreview() {
    CourseListScreen()
}