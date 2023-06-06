package com.example.newsapp.view.ui.navigation.graphs

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.newsapp.service.data.database.FavoriteDatabase
import com.example.newsapp.service.repository.FavoritesRepository
import com.example.newsapp.view.ui.navigation.BottomBarItem
import com.example.newsapp.view.ui.screens.detail.DetailScreen
import com.example.newsapp.view.ui.screens.favorites.FavoritesScreen
import com.example.newsapp.view.ui.screens.search.SearchScreen
import com.example.newsapp.viewmodel.FavoritesViewModel
import com.example.newsapp.viewmodel.SearchViewModel

@Composable
fun HomeNavGraph(navController: NavHostController) {
    val localContext = LocalContext.current
    val db =
        Room.databaseBuilder(localContext, FavoriteDatabase::class.java, "favorites_db").build()
    val dao = db.dao
    val repository = FavoritesRepository(dao)
    val favoritesViewModel = FavoritesViewModel(repository)
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = BottomBarItem.Search.route
    ) {
        composable(route = BottomBarItem.Search.route) {
            val searchViewModel = SearchViewModel()
            SearchScreen(searchViewModel, favoritesViewModel, navController)
        }
        composable(route = BottomBarItem.Favorites.route) {
            FavoritesScreen(favoritesViewModel, navController)
        }
        composable(route = "detail/{webTitle}/{thumbnail}/{bodyText}",
            arguments = listOf(
                navArgument("webTitle") { type = NavType.StringType },
                navArgument("thumbnail") { type = NavType.StringType },
                navArgument("bodyText") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val webTitle = backStackEntry.arguments?.getString("webTitle").toString()
            val thumbnail =
                Uri.parse(Uri.decode(backStackEntry.arguments?.getString("thumbnail"))).toString()
            val bodyText = backStackEntry.arguments?.getString("bodyText").toString()
            DetailScreen(webTitle, thumbnail, bodyText)
        }
    }
}
