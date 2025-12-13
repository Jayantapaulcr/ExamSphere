package com.noveletytech.examsphere.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.noveletytech.examsphere.categories.CategoriesScreen
import com.noveletytech.examsphere.favorites.FavoritesScreen
import com.noveletytech.examsphere.home.HomeScreen
import com.noveletytech.examsphere.profile.ProfileScreen
import com.noveletytech.examsphere.quiz.QuizScreen
import com.noveletytech.examsphere.result.ResultScreen

object Graph {
    const val ROOT = "root_graph"
    const val BOTTOM_BAR = "bottom_bar_graph"
    const val DETAILS = "details_graph"
}

@Composable
fun AppNavigation(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = Graph.BOTTOM_BAR,
        route = Graph.ROOT,
        modifier = Modifier.padding(paddingValues)
    ) {
        navigation(
            route = Graph.BOTTOM_BAR,
            startDestination = Screens.HOME_SCREEN
        ) {
            composable(Screens.HOME_SCREEN) {
                HomeScreen(
                    onPlayClicked = { categoryId -> navController.navigate("${Graph.DETAILS}/$categoryId") },
                    onSeeAllClicked = { navController.navigate(Screens.CATEGORIES_SCREEN) }
                )
            }
            composable(Screens.CATEGORIES_SCREEN) {
                CategoriesScreen(
                    onCategoryClicked = { categoryId -> navController.navigate("${Graph.DETAILS}/$categoryId") }
                )
            }
            composable(Screens.FAVORITES_SCREEN) {
                FavoritesScreen()
            }
            composable(Screens.PROFILE_SCREEN) {
                ProfileScreen()
            }
        }

        navigation(
            route = "${Graph.DETAILS}/{categoryId}",
            startDestination = Screens.QUIZ_SCREEN,
            arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
        ) {
            composable(Screens.QUIZ_SCREEN) { backStackEntry ->
                val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
                QuizScreen(categoryId = categoryId, onSeeResult = {
                    navController.navigate(Screens.RESULT_SCREEN)
                })
            }
            composable(Screens.RESULT_SCREEN) {
                ResultScreen(onBackToHome = {
                    navController.navigate(Screens.HOME_SCREEN) {
                        popUpTo(Graph.BOTTOM_BAR)
                    }
                })
            }
        }
    }
}