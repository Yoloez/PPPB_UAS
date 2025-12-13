package com.example.pppb_uas.ui.Dosen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack // Fix Deprecated Icon
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.PowerSettingsNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage // Perlu library Coil
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
        if (listState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.White)
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(paddingValues).fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(listState.dosenList) { dosen ->
                    DosenItemContainer(
                        dosen = dosen,
                        onToggleStatus = { viewModel.toggleDosenStatus(token, dosen.id) }
                    )
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
                    Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
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