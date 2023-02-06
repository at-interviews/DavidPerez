package com.dbperez.alltrailslunch.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dbperez.alltrailslunch.ui.home.HomeScreen

const val HOME_SCREEN_ROUTE = "home"

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = HOME_SCREEN_ROUTE) {
        composable(HOME_SCREEN_ROUTE) { HomeScreen() }
    }
}