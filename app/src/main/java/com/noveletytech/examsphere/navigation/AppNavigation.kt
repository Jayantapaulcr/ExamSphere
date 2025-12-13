package com.noveletytech.examsphere.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.noveletytech.examsphere.auth.LoginScreen
import com.noveletytech.examsphere.auth.SignUpScreen
import com.noveletytech.examsphere.categories.CategoriesScreen
import com.noveletytech.examsphere.favorites.FavoritesScreen
import com.noveletytech.examsphere.home.HomeScreen
import com.noveletytech.examsphere.home.HomeViewModel
import com.noveletytech.examsphere.profile.ProfileScreen
import com.noveletytech.examsphere.quiz.QuizScreen
import com.noveletytech.examsphere.branches.BranchesScreen
import com.noveletytech.examsphere.subcategory.SubjectsScreen
import com.noveletytech.examsphere.quiz.QuestionTypeScreen
import com.noveletytech.examsphere.ui.SplashScreen

object Graph {
    const val ROOT = "root_graph"
    const val BOTTOM_BAR = "bottom_bar_graph"
    const val DETAILS = "details_graph"
    const val AUTH = "auth_graph"
}

@Composable
fun AppNavigation(navController: NavHostController, paddingValues: PaddingValues) {
    // Get the shared HomeViewModel to access remote config
    val homeViewModel: HomeViewModel = viewModel()
    val config by homeViewModel.appConfig.collectAsState()
    val transitionDuration = config.transitionSpeed.toInt()

    NavHost(
        navController = navController,
        startDestination = Screens.SPLASH_SCREEN,
        route = Graph.ROOT,
        modifier = Modifier.padding(paddingValues),
        // Global transition animations using dynamic speed
        enterTransition = {
            slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(transitionDuration)) + fadeIn(animationSpec = tween(transitionDuration))
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(transitionDuration)) + fadeOut(animationSpec = tween(transitionDuration))
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(transitionDuration)) + fadeIn(animationSpec = tween(transitionDuration))
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(transitionDuration)) + fadeOut(animationSpec = tween(transitionDuration))
        }
    ) {
        composable(Screens.SPLASH_SCREEN) {
            SplashScreen(onNextScreen = {
                val nextDestination = if (FirebaseAuth.getInstance().currentUser != null) Graph.BOTTOM_BAR else Graph.AUTH
                navController.navigate(nextDestination) {
                    popUpTo(Screens.SPLASH_SCREEN) { inclusive = true }
                }
            })
        }

        navigation(
            route = Graph.AUTH,
            startDestination = Screens.LOGIN_SCREEN
        ) {
            composable(Screens.LOGIN_SCREEN) {
                LoginScreen(
                    onLoginSuccess = { 
                        navController.navigate(Graph.BOTTOM_BAR) {
                            popUpTo(Graph.ROOT)
                        }
                    },
                    onSignUpClicked = { navController.navigate(Screens.SIGN_UP_SCREEN) }
                )
            }
            composable(Screens.SIGN_UP_SCREEN) {
                SignUpScreen(
                    onSignUpSuccess = { navController.popBackStack() },
                    onLoginClicked = { navController.popBackStack() }
                )
            }
        }

        navigation(
            route = Graph.BOTTOM_BAR,
            startDestination = Screens.HOME_SCREEN
        ) {
            composable(Screens.HOME_SCREEN) { backStackEntry ->
                val sharedViewModel = backStackEntry.sharedViewModel<HomeViewModel>(navController)
                HomeScreen(
                    onPlayClicked = { categoryId -> navController.navigate("${Screens.BRANCHES_SCREEN}/$categoryId") },
                    onSeeAllClicked = {
                        navController.navigate(Screens.CATEGORIES_SCREEN) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    },
                    viewModel = sharedViewModel
                )
            }
            composable(Screens.CATEGORIES_SCREEN) {
                CategoriesScreen(
                    onCategoryClicked = { categoryId -> navController.navigate("${Screens.BRANCHES_SCREEN}/$categoryId") }
                )
            }
            composable(Screens.FAVORITES_SCREEN) {
                FavoritesScreen()
            }
            composable(Screens.PROFILE_SCREEN) { backStackEntry ->
                val sharedViewModel = backStackEntry.sharedViewModel<HomeViewModel>(navController)
                ProfileScreen(
                    onSignOut = { 
                        navController.navigate(Graph.AUTH) {
                            popUpTo(Graph.ROOT)
                        }
                    },
                    viewModel = sharedViewModel
                )
            }
        }

        composable("${Screens.BRANCHES_SCREEN}/{categoryId}", arguments = listOf(navArgument("categoryId") { type = NavType.StringType })) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            BranchesScreen(categoryId = categoryId, onBranchClicked = { catId, branchId ->
                navController.navigate("${Screens.SUBJECTS_SCREEN}/$catId/$branchId")
            })
        }

        composable(
            "${Screens.SUBJECTS_SCREEN}/{categoryId}/{branchId}",
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType },
                navArgument("branchId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            val branchId = backStackEntry.arguments?.getString("branchId") ?: ""
            SubjectsScreen(categoryId = categoryId, branchId = branchId, onSubjectClicked = { catId, brId, subjectId ->
                navController.navigate("${Screens.QUESTION_TYPE_SCREEN}/$catId/$brId/$subjectId")
            })
        }

        composable(
            "${Screens.QUESTION_TYPE_SCREEN}/{categoryId}/{branchId}/{subjectId}",
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType },
                navArgument("branchId") { type = NavType.StringType },
                navArgument("subjectId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            val branchId = backStackEntry.arguments?.getString("branchId") ?: ""
            val subjectId = backStackEntry.arguments?.getString("subjectId") ?: ""
            QuestionTypeScreen(onTypeSelected = { type ->
                navController.navigate("${Graph.DETAILS}/$categoryId/$branchId/$subjectId/$type")
            })
        }

        navigation(
            route = "${Graph.DETAILS}/{categoryId}/{branchId}/{subjectId}/{questionType}",
            startDestination = Screens.QUIZ_SCREEN,
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType },
                navArgument("branchId") { type = NavType.StringType },
                navArgument("subjectId") { type = NavType.StringType },
                navArgument("questionType") { type = NavType.StringType }
            )
        ) {
            composable(Screens.QUIZ_SCREEN) { backStackEntry ->
                val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
                val branchId = backStackEntry.arguments?.getString("branchId") ?: ""
                val subjectId = backStackEntry.arguments?.getString("subjectId") ?: ""
                val questionType = backStackEntry.arguments?.getString("questionType") ?: ""
                QuizScreen(
                    categoryId = categoryId, 
                    branchId = branchId,
                    subjectId = subjectId, 
                    questionType = questionType,
                    onFinishQuiz = {
                        navController.navigate(Graph.BOTTOM_BAR) {
                            popUpTo(Graph.ROOT) {
                                inclusive = false
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavHostController): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}