package com.example.newdawn.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object FindWork : Screen("findWork")
    object PostJob : Screen("postJob")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
    object MyJobs : Screen("myJobs")

    object JobDetails : Screen("jobDetails/{jobId}") {
        fun createRoute(jobId: Int) = "jobDetails/$jobId"
    }
    object SubmitOffer : Screen("submitOffer/{jobId}/{jobTitle}") {
        fun createRoute(jobId: Int, jobTitle: String) =
            "submitOffer/$jobId/${java.net.URLEncoder.encode(jobTitle, "UTF-8")}"
    }
    object ViewOffers : Screen("viewOffers/{jobId}") {
        fun createRoute(jobId: Int) = "viewOffers/$jobId"
    }
    object JobProgress : Screen("jobProgress/{jobId}") {
        fun createRoute(jobId: Int) = "jobProgress/$jobId"
    }
    object EditProfile : Screen("editProfile")
}