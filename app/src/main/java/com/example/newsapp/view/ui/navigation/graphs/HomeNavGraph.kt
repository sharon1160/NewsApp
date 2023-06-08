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
import com.example.newsapp.service.repository.NewsRepositoryImpl
import com.example.newsapp.service.repository.NewsService
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
    val favoritesRepository = FavoritesRepository(dao)
    val favoritesViewModel = FavoritesViewModel(favoritesRepository)
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = BottomBarItem.Search.route
    ) {
        composable(route = BottomBarItem.Search.route) {
            val newsRepository = NewsRepositoryImpl(NewsService.create())
            val searchViewModel = SearchViewModel(newsRepository, favoritesRepository)
            SearchScreen(searchViewModel, favoritesViewModel, navController)
        }
        composable(route = BottomBarItem.Favorites.route) {
            FavoritesScreen(favoritesViewModel, navController)
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
