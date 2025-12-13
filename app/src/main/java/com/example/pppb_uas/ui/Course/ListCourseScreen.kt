package com.example.pppb_uas.ui.Course

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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

import com.example.pppb_uas.model.Subject
import com.example.pppb_uas.viewmodel.CourseViewModel

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun CourseListScreen(
    navController: NavController,
    viewModel: CourseViewModel = viewModel(),
    token: String
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    // PERBAIKAN DI SINI:
    // 1. Gunakan 'token' sebagai key, bukan Unit. Agar jika token berubah (dari kosong ke isi), ini jalan ulang.
    // 2. Cek if (token.isNotEmpty()) agar tidak request ke server kalau token belum ada.
    LaunchedEffect(token) {
        if (token.isNotEmpty()) {
            viewModel.getCourses(token)
        }
    }

    // Refresh otomatis saat kembali ke halaman (tetap dipertahankan)
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (token.isNotEmpty()) {
                    viewModel.getCourses(token)
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // ... sisa kode ke bawah sama persis ...


    val courses = viewModel.courses
    val isLoading = viewModel.isLoading.value
    val errorMessage = viewModel.errorMessage.value

    var showDeleteDialog by remember { mutableStateOf(false) }
    var courseToDelete by remember { mutableStateOf<Subject?>(null) }

    val darkGreen = Color(0xFF015023)
    val creamColor = Color(0xFFEFE7D3)
    val yellowColor = Color(0xFFDABC4E)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Course List", color = Color.White, fontSize = 20.sp)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("add_course") }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Course",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = darkGreen)
            )
        },
        containerColor = darkGreen
    ) { paddingValues ->

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
        ) {
            when {
                // Loading State
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = creamColor
                    )
                }

                // Error State - Server error atau koneksi gagal
                // Urutan dipindah ke atas agar Error dicek sebelum Empty
                !isLoading && errorMessage.isNotEmpty() -> {
                    ErrorStateView(
                        errorMessage = errorMessage,
                        onRetryClick = { viewModel.getCourses(token) },
                        creamColor = creamColor
                    )
                }

                // Empty State - Data belum ada (dan tidak error)
                !isLoading && courses.isEmpty() -> {
                    EmptyStateView(
                        onAddClick = { navController.navigate("add_course") },
                        creamColor = creamColor
                    )
                }

                // Success State - Ada data
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(vertical = 20.dp)
                    ) {
                        itemsIndexed(courses) { index, course ->
                            CourseCard(
                                subject = course,
                                backgroundColor = creamColor,
                                onEditClick = {
                                    // Encode parameter
                                    val encodedName = java.net.URLEncoder.encode(course.nameSubject, "UTF-8")
                                    val encodedCode = java.net.URLEncoder.encode(course.codeSubject, "UTF-8")

                                    navController.navigate(
                                        "edit_course/${course.id}/$encodedName/$encodedCode/${course.sks}"
                                    )
                                },
                                onDeleteClick = {
                                    courseToDelete = course
                                    showDeleteDialog = true
                                }
                            )
                        }
                    }
                }
            }

            // Delete Dialog
            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showDeleteDialog = false
                        courseToDelete = null
                    },
                    title = { Text(text = "Hapus Mata Kuliah") },
                    text = {
                        Text(text = "Yakin ingin menghapus ${courseToDelete?.nameSubject}?")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                courseToDelete?.let { subject ->
                                    viewModel.deleteCourse(token, subject.id)
                                }
                                showDeleteDialog = false
                                courseToDelete = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("Hapus", color = Color.White)
                        }
                    },
                    dismissButton = {
                        OutlinedButton(
                            onClick = {
                                showDeleteDialog = false
                                courseToDelete = null
                            },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = darkGreen),
                            border = androidx.compose.foundation.BorderStroke(1.dp, darkGreen)
                        ) {
                            Text("Batal", color = darkGreen)
                        }
                    },
                    containerColor = creamColor,
                    titleContentColor = darkGreen,
                    textContentColor = Color.Black
                )
            }
        }
    }
}

// ... Bagian EmptyStateView, ErrorStateView, dan CourseCard tetap sama seperti kodemu ...
// Pastikan menyalin fungsi-fungsi composable tersebut juga di bawah sini.

@Composable
fun EmptyStateView(
    onAddClick: () -> Unit,
    creamColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = null,
            tint = creamColor.copy(alpha = 0.6f),
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Belum Ada Course",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = creamColor,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Mulai tambahkan mata kuliah pertama Anda",
            fontSize = 14.sp,
            color = creamColor.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onAddClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = creamColor
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.height(50.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color(0xFF015023),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Tambah Course",
                color = Color(0xFF015023),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun ErrorStateView(
    errorMessage: String,
    onRetryClick: () -> Unit,
    creamColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = Color(0xFFFFB74D),
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Terjadi Kesalahan",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = creamColor,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = errorMessage,
            fontSize = 14.sp,
            color = creamColor.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onRetryClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = creamColor
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.height(50.dp)
        ) {
            Text(
                text = "Coba Lagi",
                color = Color(0xFF015023),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun CourseCard(
    subject: Subject,
    backgroundColor: Color,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val darkGreen = Color(0xFF0D5C2F)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = darkGreen,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = subject.nameSubject,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = darkGreen,
                    maxLines = 1
                )

                Text(
                    text = "Code: ${subject.codeSubject}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = darkGreen.copy(alpha = 0.8f)
                )

                Text(
                    text = "${subject.sks} SKS",
                    fontSize = 13.sp,
                    color = darkGreen.copy(alpha = 0.7f)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = darkGreen,
                        modifier = Modifier.size(22.dp)
                    )
                }

                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color(0xFFB00020),
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}