package com.example.newsapp.view.ui.screens.favorites

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.example.newsapp.service.model.New
import com.example.newsapp.view.ui.theme.NewsAppTheme
import com.example.newsapp.view.ui.theme.Roboto
import com.example.newsapp.viewmodel.FavoritesViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import kotlin.math.absoluteValue

@Composable
fun FavoritesScreen(favoritesViewModel: FavoritesViewModel) {
    NewsAppTheme {
        FavoritesContent(
            favoritesViewModel::getAllFavorites,
            /*
            searchViewModel::searchByMovie,
            onClickItem,
            favoriteMovieViewModel::updateMovieDetail*/
        )
    }
}

@Composable
fun FavoritesContent(
    getFavorites: () -> List<New>,
    /*
    searchByMovie: (String) -> Unit,
    onClickItem: () -> Job,
    updateMovieDetail: (Movie) -> Unit*/
) {
    Column(
        modifier = Modifier.padding(top = 20.dp)
    ) {
        CarouselCard(getFavorites, /*searchByMovie, onClickItem, updateMovieDetail*/)
    }
}


@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun CarouselCard(
    getFavorites: () -> List<New>,
    /*
    searchByMovie: (String) -> Unit,
    onClickItem: () -> Job,
    updateMovieDetail: (Movie) -> Unit*/
) {
    val pagerState = rememberPagerState(initialPage = 2)
    val newsList = getFavorites()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        HorizontalPager(
            count = newsList.size,
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 80.dp),
            modifier = Modifier
                .height(750.dp)
        ) { page ->
            Card(
                modifier = Modifier
                    .height(420.dp)
                    .width(250.dp)
                    .clickable {
                        /*
                        searchByMovie(moviesList[page].imdbID)
                        updateMovieDetail(moviesList[page])
                        onClickItem()*/
                    }
                    .graphicsLayer {
                        val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                        lerp(
                            start = 0.80f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                            .also { scale ->
                                scaleX = scale
                                scaleY = scale
                            }
                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    },
                shape = MaterialTheme.shapes.large
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    /*
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(moviesList[page].poster)
                            .crossfade(true)
                            .scale(Scale.FILL)
                            .build(),
                        modifier = Modifier
                            .padding(bottom = 20.dp)
                            .height(300.dp)
                            .width(280.dp)
                            .clip(MaterialTheme.shapes.medium),
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )*/
                    Text(
                        text = newsList[page].webTitle,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Roboto,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(text = newsList[page].webPublicationDate, fontFamily = Roboto)
                }
            }
        }
    }
}
