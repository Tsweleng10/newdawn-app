package com.example.newdawn.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.newdawn.ui.auth.LoginScreen
import com.example.newdawn.ui.auth.RegisterScreen
import com.example.newdawn.ui.auth.SplashScreen
import com.example.newdawn.ui.offers.MyJobsScreen
import com.example.newdawn.ui.offers.SubmitOfferScreen
import com.example.newdawn.ui.offers.ViewOffersScreen
import com.example.newdawn.ui.profile.ProfileScreen
import com.example.newdawn.ui.profile.SettingsScreen
import com.example.newdawn.utils.TokenManager
import kotlinx.coroutines.launch
import com.example.newdawn.ui.home.HomeScreen
import com.example.newdawn.ui.jobs.FindWorkScreen
import com.example.newdawn.ui.jobs.PostJobScreen
import com.example.newdawn.ui.jobs.JobDetailsScreen
import com.example.newdawn.ui.profile.EditProfileScreen


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val tokenManager = TokenManager(context.applicationContext)

    NavHost(navController = navController, startDestination = Screen.Splash.route) {

        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onBackToLogin = { navController.popBackStack() }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(Screen.FindWork.route) {
            FindWorkScreen(navController = navController)
        }

        composable(Screen.PostJob.route) {
            PostJobScreen(
                onJobPosted = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }


        composable(
            route = Screen.JobDetails.route,
            arguments = listOf(navArgument("jobId") { type = NavType.IntType })
        ) { entry ->
            val jobId = entry.arguments?.getInt("jobId") ?: 0
            JobDetailsScreen(jobId = jobId, navController = navController)
        }

        composable(
            route = Screen.SubmitOffer.route,
            arguments = listOf(
                navArgument("jobId") { type = NavType.IntType },
                navArgument("jobTitle") { type = NavType.StringType }
            )
        ) { entry ->
            val jobId = entry.arguments?.getInt("jobId") ?: 0
            val jobTitle = entry.arguments?.getString("jobTitle") ?: ""
            SubmitOfferScreen(
                jobId = jobId,
                jobTitle = jobTitle,
                onSubmitted = { navController.popBackStack() }
            )
        }

        composable(Screen.MyJobs.route) {
            MyJobsScreen(
                onJobClick = { jobId ->
                    navController.navigate(Screen.ViewOffers.createRoute(jobId))
                }
            )
        }

        composable(
            route = Screen.ViewOffers.route,
            arguments = listOf(navArgument("jobId") { type = NavType.IntType })
        ) { entry ->
            val jobId = entry.arguments?.getInt("jobId") ?: 0
            ViewOffersScreen(jobId = jobId)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onNavigateToMyJobs = { navController.navigate(Screen.MyJobs.route) },
                onNavigateToEditProfile = { navController.navigate(Screen.EditProfile.route) },
                onLogout = {
                    scope.launch {
                        tokenManager.clear()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onLogout = {
                    scope.launch {
                        tokenManager.clear()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            )
        }
        composable(Screen.EditProfile.route) {
            EditProfileScreen(
                onSaved = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
    }
}