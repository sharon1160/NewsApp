package com.example.newsapp.ui.navigation.graphs

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.newsapp.ui.navigation.BottomBarItem
import com.example.newsapp.ui.detail.DetailScreen
import com.example.newsapp.ui.favorites.FavoritesScreen
import com.example.newsapp.ui.search.SearchScreen

@Composable
fun HomeNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = BottomBarItem.Search.route
    ) {
        composable(route = BottomBarItem.Search.route) {
            SearchScreen(navController = navController)
        }
        composable(route = BottomBarItem.Favorites.route) {
            FavoritesScreen(navController = navController)
        }
        composable(route = "detail/{webUrl}",
            arguments = listOf(
                navArgument("webUrl") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val webUrl =
                Uri.parse(Uri.decode(backStackEntry.arguments?.getString("webUrl"))).toString()
            DetailScreen(webUrl)
        }
    }
}
