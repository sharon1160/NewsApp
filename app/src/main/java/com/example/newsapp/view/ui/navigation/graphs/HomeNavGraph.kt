package com.example.newsapp.view.ui.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.newsapp.view.ui.navigation.BottomBarItem
import com.example.newsapp.view.ui.screens.detail.DetailScreen
import com.example.newsapp.view.ui.screens.favorites.FavoritesScreen
import com.example.newsapp.view.ui.screens.search.SearchScreen

@Composable
fun HomeNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = BottomBarItem.Search.route
    ) {
        composable(route = BottomBarItem.Search.route) {
            SearchScreen()
        }
        composable(route = BottomBarItem.Favorites.route) {
            FavoritesScreen()
        }
        composable(route = "detail") {
            DetailScreen()
        }
    }
}
