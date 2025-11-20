package com.example.pppb_uas

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pppb_uas.preferences.PreferencesManager
import com.example.pppb_uas.navigation.Screen
import com.example.pppb_uas.ui.screen.DashboardScreen
import com.example.pppb_uas.ui.theme.PPPB_UASTheme
import com.example.pppb_uas.viewmodel.LoginState
import com.example.pppb_uas.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PPPB_UASTheme {
                val navController = rememberNavController()
                val context = LocalContext.current
                val preferencesManager = remember { PreferencesManager(context) }

                NavHost(
                    navController = navController,
                    startDestination = Screen.Login.route
                ) {
                    composable(Screen.Login.route) {
                        LoginScreen(
                            navController = navController,
                            preferencesManager = preferencesManager
                        )
                    }

                    composable(Screen.Dashboard.route) {
                        val userName by preferencesManager.userName.collectAsState(initial = "")
                        val userEmail by preferencesManager.userEmail.collectAsState(initial = "")
                        val scope = rememberCoroutineScope()

                        DashboardScreen(
                            userName = userName ?: "User",
                            userEmail = userEmail ?: "",
                            onCourseClick = { /* Navigate to course */ },
                            onLecturerClick = { /* Navigate to lecturer */ },
                            onStudentClick = { /* Navigate to student */ },
                            onLogout = {
                                scope.launch {
                                    preferencesManager.clearAuthData()
                                    navController.navigate("login") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                    Toast.makeText(
                                        context,
                                        "Logout berhasil",
                                        Toast.LENGTH_SHORT
                                    ).show()
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
fun LoginScreen(
    navController: NavHostController,
    preferencesManager: PreferencesManager,
    viewModel: LoginViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val loginState by viewModel.loginState.collectAsState()

    LaunchedEffect(loginState) {
        when (val state = loginState) {
            is LoginState.Success -> {
                val userData = state.response.data
                if (userData != null) {
                    // Simpan data user
                    preferencesManager.saveAuthData(
                        token = userData.access_token,
                        name = userData.user.name,
                        email = userData.user.email,
                        userId = userData.user.id.toString()
                    )

                    Toast.makeText(
                        context,
                        "Login berhasil! Welcome ${userData.user.name}",
                        Toast.LENGTH_LONG
                    ).show()

                    // Navigate ke dashboard
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
                viewModel.resetState()
            }
            is LoginState.Error -> {
                Toast.makeText(
                    context,
                    "Login gagal: ${state.message}",
                    Toast.LENGTH_LONG
                ).show()
                viewModel.resetState()
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF015023))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Image(
                painter = painterResource(R.drawable.logo_ugn),
                contentDescription = "UGN Logo",
                modifier = Modifier.size(240.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Login to your account",
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                enabled = loginState !is LoginState.Loading,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                ),
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                enabled = loginState !is LoginState.Loading,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                ),
                shape = RoundedCornerShape(8.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(82.dp))

            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        viewModel.login(email, password)
                    } else {
                        Toast.makeText(context, "Email dan password harus diisi", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = loginState !is LoginState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD4AF37)
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                if (loginState is LoginState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.Black
                    )
                } else {
                    Text(
                        text = "Login",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}