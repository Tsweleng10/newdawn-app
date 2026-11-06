package com.example.newdawn.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.newdawn.R
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.newdawn.ui.auth.LoginScreen
import com.example.newdawn.ui.auth.RegisterScreen
import com.example.newdawn.ui.auth.SplashScreen
import com.example.newdawn.ui.home.HomeScreen
import com.example.newdawn.ui.jobs.FindWorkScreen
import com.example.newdawn.ui.jobs.JobDetailsScreen
import com.example.newdawn.ui.jobs.PostJobScreen
import com.example.newdawn.ui.offers.MyJobsScreen
import com.example.newdawn.ui.offers.SubmitOfferScreen
import com.example.newdawn.ui.offers.ViewOffersScreen
import com.example.newdawn.ui.profile.EditProfileScreen
import com.example.newdawn.ui.profile.ProfileScreen
import com.example.newdawn.ui.profile.SettingsScreen
import com.example.newdawn.utils.TokenManager
import kotlinx.coroutines.launch

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val tokenManager = TokenManager(context.applicationContext)

    // Get current route to highlight the active bottom icon
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Define the 4 main tabs for the bottom bar
    val bottomNavItems = listOf(
        BottomNavItem(Screen.Home.route, Icons.Filled.Home, stringResource(R.string.nav_home)),
        BottomNavItem(Screen.FindWork.route, Icons.Filled.Search, stringResource(R.string.nav_find_work)),
        BottomNavItem(Screen.PostJob.route, Icons.Filled.AddCircle, stringResource(R.string.nav_post_job)),
        BottomNavItem(Screen.Profile.route, Icons.Filled.Person, stringResource(R.string.nav_profile))
    )

    // Only show bottom bar on these specific screens
    val showBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.FindWork.route,
        Screen.PostJob.route,
        Screen.Profile.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    // Pop up to the start destination to avoid building a huge stack
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
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
}