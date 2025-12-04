package com.example.pppb_uas.ui.Dosen

import com.example.pppb_uas.model.Dosen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
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
fun LecturerListScreen(
    onBackClick: () -> Unit = {},
    onAddClick: () -> Unit = {},
    onEditClick: (Dosen) -> Unit = {},
    onDeleteClick: (Dosen) -> Unit = {}
) {
    // Sample data
    val lecturers = remember {
        mutableStateListOf(
            Dosen(id = "1", name = "Dinar. S.Kom", nip = "12345678910"),
            Dosen(id = "2", name = "Tono. S.Kom", nip = "9876543210"),
            Dosen(id = "3", name = "Tofo. S.Kom", nip = "6534782910"),
            Dosen(id = "4", name = "Toro. S.Kom", nip = "7865432109"),
            Dosen(id = "5", name = "Toro. S.Kom", nip = "7865435109")
        )
    }

    // --- STATE BARU UNTUK ALERT DIALOG ---
    var showDeleteDialog by remember { mutableStateOf(false) }
    var dosenToDelete by remember { mutableStateOf<Dosen?>(null) }
    // -------------------------------------

    val darkGreen = Color(0xFF015023)
    val creamColor = Color(0xFFEFE7D3)
    val yellowColor = Color(0xFFDABC4E)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dosen", color = Color.White, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = onAddClick) {
                        Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White, modifier = Modifier.size(28.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = darkGreen)
            )
        },
        containerColor = darkGreen
    ) { paddingValues ->

        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 20.dp)
            ) {
                itemsIndexed(lecturers) { index, lecturer ->
                    LecturerCard(
                        lecturer = lecturer,
                        isSelected = index == 1,
                        backgroundColor = if (index == 1) yellowColor else creamColor,
                        onEditClick = { onEditClick(lecturer) },
                        // UBAH LOGIKA DELETE DI SINI:
                        onDeleteClick = {
                            dosenToDelete = lecturer // 1. Simpan dosen yang mau dihapus
                            showDeleteDialog = true  // 2. Tampilkan dialog
                        }
                    )
                }
            }

            // --- KODE LOGIKA ALERT DIALOG ---
            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = {
                        Text(text = "Hapus Data Dosen")
                    },
                    text = {
                        Text(text = "Apakah anda yakin ingin menghapus data ${dosenToDelete?.name}?")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                // Panggil fungsi delete yang sebenarnya
                                dosenToDelete?.let { onDeleteClick(it) }
                                // Reset state
                                showDeleteDialog = false
                                dosenToDelete = null
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
                                dosenToDelete = null
                            }
                        ) {
                            Text("Batal", color = darkGreen)
                        }
                    },
                    containerColor = creamColor, // Menyesuaikan tema
                    titleContentColor = darkGreen,
                    textContentColor = Color.Black
                )
            }
        }
    }
}

// ... (LecturerCard dan Preview tetap sama, tidak perlu diubah) ...
@Composable
fun LecturerCard(
    lecturer: Dosen,
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
            // Profile Image
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = lecturer.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = lecturer.nip,
                    fontSize = 14.sp,
                    color = textColor.copy(alpha = 0.8f)
                )
            }

            // Action Buttons
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

@Preview(showBackground = true)
@Composable
fun LecturerListScreenPreview() {
    LecturerListScreen()
}