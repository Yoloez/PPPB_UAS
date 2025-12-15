package com.example.pppb_uas.ui.mahasiswa

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.PowerSettingsNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pppb_uas.R // Pastikan import R sesuai package
import com.example.pppb_uas.model.Mahasiswa
import com.example.pppb_uas.preferences.PreferencesManager
import com.example.pppb_uas.viewmodel.MahasiswaViewModel

// Definisi Font (Private)
private val urbanistFontFamily = FontFamily(
    Font(R.font.urbanist_regular, FontWeight.Normal),
    Font(R.font.urbanist_medium, FontWeight.Medium),
    Font(R.font.urbanist_semibold, FontWeight.SemiBold),
    Font(R.font.urbanist_bold, FontWeight.Bold)
)

// --- Warna Sesuai Desain ---
private val DarkGreen = Color(0xFF015023)
private val CreamItem = Color(0xFFF5E6D3)
private val StatusActive = Color(0xFF4CAF50) // Hijau
private val StatusInactive = Color(0xFFE53935) // Merah

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListMahasiswaScreen(
    onBackClick: () -> Unit = {},
    onAddClick: () -> Unit = {},
    viewModel: MahasiswaViewModel = viewModel()
) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }

    val token by preferencesManager.token.collectAsState(initial = "")
    val listState by viewModel.listUiState.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(token) {
        if (token.isNotEmpty()) {
            viewModel.fetchMahasiswa(token)
        }
    }

    val filteredMahasiswaList = remember(listState.listMahasiswa, searchQuery) {
        if (searchQuery.isBlank()) {
            listState.listMahasiswa
        } else {
            listState.listMahasiswa.filter {
                (it.name?.contains(searchQuery, ignoreCase = true) ?: false) ||
                        (it.username?.contains(searchQuery, ignoreCase = true) ?: false) ||
                        (it.nim?.contains(searchQuery, ignoreCase = true) ?: false) ||
                        (it.programName?.contains(searchQuery, ignoreCase = true) ?: false)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "List Mahasiswa",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontFamily = urbanistFontFamily
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = onAddClick) {
                        Icon(Icons.Default.Add, "Add Student", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkGreen)
            )
        },
        containerColor = DarkGreen
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // --- SEARCH BAR ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            "Cari Mahasiswa (Nama, NIM, Prodi)...",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontFamily = urbanistFontFamily
                        )
                    },
                    singleLine = true,
                    // Terapkan Font Urbanist
                    textStyle = TextStyle(
                        fontFamily = urbanistFontFamily,
                        fontSize = 16.sp,
                        color = Color.Black
                    ),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = DarkGreen
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Clear Search",
                                    tint = Color.Gray
                                )
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        cursorColor = DarkGreen,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (listState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else if (listState.errorMessage != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = listState.errorMessage ?: "Terjadi Kesalahan",
                        color = Color.White,
                        modifier = Modifier.padding(16.dp),
                        fontFamily = urbanistFontFamily
                    )
                }
            } else if (filteredMahasiswaList.isEmpty() && searchQuery.isNotEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Tidak ditemukan mahasiswa dengan kata kunci \"$searchQuery\"",
                        color = Color.White,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp),
                        fontFamily = urbanistFontFamily
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredMahasiswaList) { mahasiswa ->
                        MahasiswaCard(
                            data = mahasiswa,
                            onToggleStatus = {
                                if (token.isNotEmpty()) {
                                    viewModel.toggleStatus(token, mahasiswa.safeId)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MahasiswaCard(
    data: Mahasiswa,
    onToggleStatus: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CreamItem),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Foto Profil
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
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

            // Info Mahasiswa
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = data.name ?: data.username ?: data.id ?: "Tanpa Nama",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = urbanistFontFamily
                )
                Text(
                    text = "NIM: ${data.nim ?: "-"}",
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    fontFamily = urbanistFontFamily
                )
                Text(
                    text = "Prodi: ${data.programName ?: "-"}",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = urbanistFontFamily
                )

                // Status Pill
                Surface(
                    color = if (data.isActiveBoolean) StatusActive else StatusInactive,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (data.isActiveBoolean) Icons.Default.CheckCircle else Icons.Default.Cancel,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = if (data.isActiveBoolean) "AKTIF" else "NON-AKTIF",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = urbanistFontFamily
                        )
                    }
                }
            }

            // Tombol Power
            IconButton(onClick = onToggleStatus) {
                Icon(
                    imageVector = Icons.Outlined.PowerSettingsNew,
                    contentDescription = "Toggle Status",
                    tint = if (data.isActiveBoolean) StatusActive else StatusInactive,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}