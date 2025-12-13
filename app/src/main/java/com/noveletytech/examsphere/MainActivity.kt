package com.noveletytech.examsphere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.noveletytech.examsphere.home.HomeViewModel
import com.noveletytech.examsphere.navigation.AppNavigation
import com.noveletytech.examsphere.navigation.BottomNavigationBar
import com.noveletytech.examsphere.navigation.Graph
import com.noveletytech.examsphere.ui.MaintenanceScreen
import com.noveletytech.examsphere.ui.theme.ExamSphereTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val homeViewModel: HomeViewModel = viewModel()
            val config by homeViewModel.appConfig.collectAsState()
            
            // Parse primary color from Firestore, fallback to default if invalid
            val customPrimaryColor = remember(config.primaryColor) {
                try {
                    Color(android.graphics.Color.parseColor(config.primaryColor))
                } catch (e: Exception) {
                    Color(0xFF1A3B8E)
                }
            }

            ExamSphereTheme(primaryColor = customPrimaryColor) {
                if (config.maintenanceMode) {
                    MaintenanceScreen()
                } else {
                    MainScreen(homeViewModel)
                }
            }
        }
    }
}

@Composable
fun MainScreen(homeViewModel: HomeViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.parent?.route

    Scaffold(
        bottomBar = {
            if (currentRoute == Graph.BOTTOM_BAR) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { paddingValues ->
        AppNavigation(navController = navController, paddingValues = paddingValues)
    }
}
