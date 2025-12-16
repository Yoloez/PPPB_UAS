package com.example.pppb_uas.ui.Dosen

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pppb_uas.preferences.PreferencesManager
import com.example.pppb_uas.model.Dosen
import com.example.pppb_uas.viewmodel.DosenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DosenListScreen(
    viewModel: DosenViewModel,
    onAddClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val listState by viewModel.listUiState.collectAsState()
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val token by preferencesManager.token.collectAsState(initial = "")
    val DarkGreen = Color(0xFF015023)

    // --- State untuk Query Pencarian (Logic Tetap) ---
    var searchQuery by remember { mutableStateOf("") }

    // --- Filter List Dosen (Logic Tetap) ---
    val filteredDosenList = remember(listState.dosenList, searchQuery) {
        if (searchQuery.isBlank()) {
            listState.dosenList
        } else {
            listState.dosenList.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                        it.email.contains(searchQuery, ignoreCase = true) ||
                        (it.nip?.contains(searchQuery, ignoreCase = true) ?: false)
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("List Dosen", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = onAddClick) {
                        Icon(Icons.Default.Add, "Add", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkGreen)
            )
        },
        containerColor = DarkGreen
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).fillMaxSize()
        ) {
            // --- TAMPILAN SEARCH BAR BARU (Putih & Shadow) ---
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
                            "Cari Dosen (Nama, Email, NIP)...",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    },
                    singleLine = true,
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
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else if (filteredDosenList.isEmpty() && searchQuery.isNotEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Tidak ditemukan dosen dengan kata kunci \"$searchQuery\"",
                        color = Color.White,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredDosenList) { dosen -> // Logic List Tetap
                        DosenItemContainer(
                            dosen = dosen,
                            onToggleStatus = { viewModel.toggleDosenStatus(token, dosen.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DosenItemContainer(dosen: Dosen, onToggleStatus: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5E6D3)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = dosen.photoUrl ?: "https://ui-avatars.com/api/?name=${dosen.name}",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(64.dp).clip(CircleShape).background(Color.Gray)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(dosen.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(dosen.email, fontSize = 14.sp, color = Color.Gray)
                Text("NIP: ${dosen.nip}", fontSize = 14.sp, color = Color.Gray)

                // Status Pill
                Surface(
                    color = if (dosen.isActive) Color(0xFF4CAF50) else Color(0xFFE53935),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            if (dosen.isActive) Icons.Default.CheckCircle else Icons.Default.Cancel,
                            null, tint = Color.White, modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(if (dosen.isActive) "AKTIF" else "NON-AKTIF", color = Color.White, fontSize = 12.sp)
                    }
                }
            }
            IconButton(onClick = onToggleStatus) {
                Icon(
                    Icons.Outlined.PowerSettingsNew, null,
                    tint = if (dosen.isActive) Color(0xFF4CAF50) else Color(0xFFE53935)
                )
            }
        }
    }
}