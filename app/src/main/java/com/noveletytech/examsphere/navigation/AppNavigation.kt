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
import com.noveletytech.examsphere.admin.AddCategoryScreen
import com.noveletytech.examsphere.admin.AddQuestionScreen
import com.noveletytech.examsphere.admin.AddSubCategoryScreen
import com.noveletytech.examsphere.admin.AdminDashboardScreen
import com.noveletytech.examsphere.admin.AdminLoginScreen
import com.noveletytech.examsphere.admin.AdminSignUpScreen
import com.noveletytech.examsphere.admin.CategoryDetailScreen
import com.noveletytech.examsphere.admin.EditCategoryScreen
import com.noveletytech.examsphere.admin.ManageCategoriesScreen
import com.noveletytech.examsphere.admin.QuestionTypeScreen
import com.noveletytech.examsphere.categories.CategoriesScreen
import com.noveletytech.examsphere.favorites.FavoritesScreen
import com.noveletytech.examsphere.home.HomeScreen
import com.noveletytech.examsphere.profile.ProfileScreen
import com.noveletytech.examsphere.quiz.QuizScreen
import com.noveletytech.examsphere.result.ResultScreen
import com.noveletytech.examsphere.subcategory.SubCategoryScreen

object Graph {
    const val ROOT = "root_graph"
    const val BOTTOM_BAR = "bottom_bar_graph"
    const val DETAILS = "details_graph"
    const val ADMIN = "admin_graph"
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
                    onPlayClicked = { categoryId -> navController.navigate("${Screens.SUB_CATEGORY_SCREEN}/$categoryId") },
                    onSeeAllClicked = { navController.navigate(Screens.CATEGORIES_SCREEN) }
                )
            }
            composable(Screens.CATEGORIES_SCREEN) {
                CategoriesScreen(
                    onCategoryClicked = { categoryId -> navController.navigate("${Screens.SUB_CATEGORY_SCREEN}/$categoryId") }
                )
            }
            composable(Screens.FAVORITES_SCREEN) {
                FavoritesScreen()
            }
            composable(Screens.PROFILE_SCREEN) {
                ProfileScreen(onAdminClicked = { navController.navigate(Graph.ADMIN) })
            }
        }

        composable("${Screens.SUB_CATEGORY_SCREEN}/{categoryId}", arguments = listOf(navArgument("categoryId") { type = NavType.StringType })) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            SubCategoryScreen(categoryId = categoryId, onSubCategoryClicked = { catId, subCatId ->
                navController.navigate("${Graph.DETAILS}/$catId/$subCatId")
            })
        }

        navigation(
            route = "${Graph.DETAILS}/{categoryId}/{subCategoryId}",
            startDestination = Screens.QUIZ_SCREEN,
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType },
                navArgument("subCategoryId") { type = NavType.StringType }
            )
        ) {
            composable(Screens.QUIZ_SCREEN) { backStackEntry ->
                val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
                val subCategoryId = backStackEntry.arguments?.getString("subCategoryId") ?: ""
                QuizScreen(categoryId = categoryId, subCategoryId = subCategoryId, onSeeResult = {
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

        navigation(
            route = Graph.ADMIN,
            startDestination = Screens.ADMIN_LOGIN_SCREEN
        ) {
            composable(Screens.ADMIN_LOGIN_SCREEN) {
                AdminLoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screens.ADMIN_DASHBOARD_SCREEN) {
                            popUpTo(Graph.ADMIN)
                        }
                    },
                    onCreateAccountClicked = { navController.navigate(Screens.ADMIN_SIGN_UP_SCREEN) }
                )
            }
            composable(Screens.ADMIN_DASHBOARD_SCREEN) {
                AdminDashboardScreen(
                    onSignOut = {
                        navController.navigate(Graph.BOTTOM_BAR) {
                            popUpTo(Graph.ROOT)
                        }
                    },
                    onManageCategories = { navController.navigate(Screens.MANAGE_CATEGORIES_SCREEN) }
                )
            }
            composable(Screens.ADMIN_SIGN_UP_SCREEN) {
                AdminSignUpScreen(onSignUpSuccess = { navController.popBackStack() })
            }
            composable(Screens.MANAGE_CATEGORIES_SCREEN) {
                ManageCategoriesScreen(
                    onAddCategory = { navController.navigate(Screens.ADD_CATEGORY_SCREEN) },
                    onEditCategory = { categoryId -> navController.navigate("${Screens.EDIT_CATEGORY_SCREEN}/$categoryId") },
                    onCategoryClicked = { categoryId -> navController.navigate("${Screens.CATEGORY_DETAIL_SCREEN}/$categoryId") }
                )
            }
            composable(Screens.ADD_CATEGORY_SCREEN) {
                AddCategoryScreen(onCategoryAdded = { navController.popBackStack() })
            }
            composable("${Screens.EDIT_CATEGORY_SCREEN}/{categoryId}", arguments = listOf(navArgument("categoryId") { type = NavType.StringType })) { backStackEntry ->
                val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
                EditCategoryScreen(categoryId = categoryId, onCategoryUpdated = { navController.popBackStack() })
            }
            composable("${Screens.CATEGORY_DETAIL_SCREEN}/{categoryId}", arguments = listOf(navArgument("categoryId") { type = NavType.StringType })) { backStackEntry ->
                val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
                CategoryDetailScreen(
                    categoryId = categoryId,
                    onAddSubCategory = { navController.navigate("${Screens.ADD_SUB_CATEGORY_SCREEN}/$categoryId") },
                    onSubCategoryClicked = { subCategoryId -> navController.navigate("${Screens.QUESTION_TYPE_SCREEN}/$categoryId/$subCategoryId") }
                )
            }
            composable("${Screens.ADD_SUB_CATEGORY_SCREEN}/{categoryId}", arguments = listOf(navArgument("categoryId") { type = NavType.StringType })) { backStackEntry ->
                val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
                AddSubCategoryScreen(categoryId = categoryId, onSubCategoryAdded = { navController.popBackStack() })
            }
            composable(
                "${Screens.QUESTION_TYPE_SCREEN}/{categoryId}/{subCategoryId}",
                arguments = listOf(
                    navArgument("categoryId") { type = NavType.StringType },
                    navArgument("subCategoryId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
                val subCategoryId = backStackEntry.arguments?.getString("subCategoryId") ?: ""
                QuestionTypeScreen(
                    onMcqClicked = { navController.navigate("${Screens.ADD_QUESTION_SCREEN}/$categoryId/$subCategoryId/mcq") },
                    onSaqClicked = { navController.navigate("${Screens.ADD_QUESTION_SCREEN}/$categoryId/$subCategoryId/saq") },
                    onLaqClicked = { navController.navigate("${Screens.ADD_QUESTION_SCREEN}/$categoryId/$subCategoryId/laq") }
                )
            }
            composable(
                "${Screens.ADD_QUESTION_SCREEN}/{categoryId}/{subCategoryId}/{questionType}", 
                arguments = listOf(
                    navArgument("categoryId") { type = NavType.StringType },
                    navArgument("subCategoryId") { type = NavType.StringType },
                    navArgument("questionType") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
                val subCategoryId = backStackEntry.arguments?.getString("subCategoryId") ?: ""
                val questionType = backStackEntry.arguments?.getString("questionType") ?: ""
                AddQuestionScreen(categoryId = categoryId, subCategoryId = subCategoryId, questionType = questionType, onQuestionAdded = { navController.popBackStack() })
            }
        }
    }
}