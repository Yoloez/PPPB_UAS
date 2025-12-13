package com.example.pppb_uas.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlinx.coroutines.launch

import com.example.pppb_uas.preferences.PreferencesManager
import com.example.pppb_uas.ui.screen.LoginScreen
import com.example.pppb_uas.ui.screen.DashboardScreen
import com.example.pppb_uas.viewmodel.CourseViewModel
import com.example.pppb_uas.viewmodel.DosenViewModel
import com.example.pppb_uas.ui.Dosen.DosenListScreen
import com.example.pppb_uas.ui.Dosen.AddDosenScreen
import com.example.pppb_uas.ui.Course.CourseListScreen
import com.example.pppb_uas.ui.Course.AddCourseScreen
import com.example.pppb_uas.ui.Course.EditCourseScreen

// Definisikan sealed class Screen jika belum ada di file terpisah

@Composable
fun AppNavGraph(
    navController: NavHostController,
    preferencesManager: PreferencesManager
) {
    val scope = rememberCoroutineScope()

    val userName by preferencesManager.userName.collectAsState(initial = "Admin")
    val userEmail by preferencesManager.userEmail.collectAsState(initial = "admin@ugn.ac.id")
    val userToken by preferencesManager.token.collectAsState(initial = "")

    val courseViewModel: CourseViewModel = viewModel()
    val dosenViewModel: DosenViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController = navController, preferencesManager = preferencesManager)
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                userName = userName ?: "User",
                userEmail = userEmail ?: "",
                onCourseClick = { navController.navigate("manage_course") },
                onLecturerClick = { navController.navigate("manage_lecturer") },
                onStudentClick = { navController.navigate("manage_student") },
                onLogout = {
                    scope.launch {
                        preferencesManager.clearAuthData()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Dashboard.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        // --- COURSE ROUTES ---
        composable("manage_course") {
            val safeToken = if (userToken.isNotEmpty()) userToken else ""
            CourseListScreen(navController = navController, viewModel = courseViewModel, token = safeToken)
        }
        composable("add_course") {
            AddCourseScreen(navController = navController, viewModel = courseViewModel, token = userToken)
        }
        composable(
            route = "edit_course/{id}/{name}/{code}/{sks}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType },
                navArgument("name") { type = NavType.StringType },
                navArgument("code") { type = NavType.StringType },
                navArgument("sks") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val code = backStackEntry.arguments?.getString("code") ?: ""
            val sks = backStackEntry.arguments?.getInt("sks") ?: 0
            EditCourseScreen(navController = navController, viewModel = courseViewModel, token = userToken, courseId = id, initialName = name, initialCode = code, initialSks = sks)
        }

        // --- DOSEN ROUTES ---
        composable("manage_lecturer") {
            val safeToken = if (userToken.isNotEmpty()) userToken else ""

            // PERBAIKAN PENTING: Fetch data saat halaman dibuka
            LaunchedEffect(Unit) {
                if (safeToken.isNotEmpty()) {
                    dosenViewModel.fetchDosenList(safeToken)
                }
            }

            DosenListScreen(
                viewModel = dosenViewModel,
                onAddClick = { navController.navigate("add_lecturer") },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("add_lecturer") {
            AddDosenScreen(
                viewModel = dosenViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("manage_student") {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Halaman Student Belum Dibuat")
            }
        }
    }
}