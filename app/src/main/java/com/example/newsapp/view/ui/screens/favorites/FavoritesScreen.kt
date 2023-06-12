package com.example.newsapp.view.ui.screens.favorites

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.example.newsapp.service.model.Fields
import com.example.newsapp.service.model.New
import com.example.newsapp.view.ui.screens.search.Message
import com.example.newsapp.view.ui.theme.NewsAppTheme
import com.example.newsapp.view.ui.theme.Roboto
import com.example.newsapp.viewmodel.FavoritesViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import org.koin.androidx.compose.koinViewModel
import kotlin.math.absoluteValue

@Composable
fun FavoritesScreen(
    favoritesViewModel: FavoritesViewModel = koinViewModel(),
    navController: NavHostController
) {
    val favoritesNews by favoritesViewModel.favoritesNews.collectAsState()

    val navigateToDetail = { webUrl: String ->
        navController.navigate(route = "detail/${Uri.encode(webUrl)}")
    }

    NewsAppTheme {
        FavoritesContent(
            favoritesNews,
            favoritesViewModel::delete,
            navigateToDetail
        )
    }
}

@Composable
fun FavoritesContent(
    favoritesList: List<New>,
    deleteFavorite: (New) -> Unit,
    navigateToDetail: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(top = 20.dp)
    ) {
        CarouselCard(
            favoritesList,
            deleteFavorite,
            navigateToDetail
        )
    }
}


@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun CarouselCard(
    favoritesList: List<New>,
    deleteFavorite: (New) -> Unit,
    navigateToDetail: (String) -> Unit
) {
    if (favoritesList.isNotEmpty()) {
        val pagerState = rememberPagerState(initialPage = 1)
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HorizontalPager(
                count = favoritesList.size,
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
                            val webUrl = favoritesList[page].webUrl
                            navigateToDetail(webUrl)
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

                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(favoritesList[page].fields.thumbnail)
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
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Box(modifier = Modifier.width(170.dp)) {
                                Text(
                                    text = favoritesList[page].webTitle,
                                    fontWeight = FontWeight.Light,
                                    fontFamily = Roboto,
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                            DeleteButton(favoritesList[page], deleteFavorite)
                        }
                    }
                }
            }
        }
    } else {
        Message("No favorites")
    }
}

@Composable
fun DeleteButton(
    new: New,
    deleteFavorite: (New) -> Unit
) {
    IconButton(
        modifier = Modifier.size(35.dp),
        onClick = {
            deleteFavorite(new)
        }
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete Icon",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview
@Composable
fun FavoritesPreview() {
    val list = mutableListOf(
        New(
            id = "", type = "", sectionId = "", sectionName = "", webPublicationDate = "",
            webTitle = "Title 1", webUrl = "", apiUrl = "",
            fields = Fields(
                "",
                "",
                "",
                ""
            ),
            isHosted = false, pillarId = "", pillarName = "", isFavorite = false
        ),
        New(
            id = "", type = "", sectionId = "", sectionName = "", webPublicationDate = "",
            webTitle = "Title 2", webUrl = "", apiUrl = "",
            fields = Fields(
                "",
                "",
                "",
                ""
            ),
            isHosted = false, pillarId = "", pillarName = "", isFavorite = false
        ),
        New(
            id = "", type = "", sectionId = "", sectionName = "", webPublicationDate = "",
            webTitle = "Title 3", webUrl = "", apiUrl = "",
            fields = Fields(
                "",
                "",
                "",
                ""
            ),
            isHosted = false, pillarId = "", pillarName = "", isFavorite = false
        )
    )
    NewsAppTheme {
        FavoritesContent(favoritesList = list,
            deleteFavorite = {},
            navigateToDetail = { webUrl: String ->
                Log.d("TAG", webUrl)
            })
    }
}
