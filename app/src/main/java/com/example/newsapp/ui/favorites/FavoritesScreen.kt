package com.example.newsapp.ui.favorites

import android.net.Uri
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.example.newsapp.R
import com.example.newsapp.data.model.Fields
import com.example.newsapp.data.model.New
import com.example.newsapp.ui.search.Message
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.newsapp.ui.theme.Roboto
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
    val detailBasePath = stringResource(R.string.detail_base_path)

    val navigateToDetail = { webUrl: String ->
        navController.navigate(route = "${detailBasePath}${Uri.encode(webUrl)}")
    }

    NewsAppTheme {
        FavoritesScreenContent(
            favoritesNews,
            favoritesViewModel::delete,
            navigateToDetail
        )
    }
}

@Composable
fun FavoritesScreenContent(
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


@OptIn(ExperimentalPagerApi::class)
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
        Message(stringResource(R.string.no_favorites_message))
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
            contentDescription = stringResource(R.string.delete_content_description),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview
@Composable
fun FavoritesScreenPreview() {
    val emptyText = stringResource(R.string.empty_text)
    val sampleText = stringResource(R.string.sample_text)
    val list = mutableListOf(
        New(
            id = emptyText,
            type = emptyText,
            sectionId = emptyText,
            sectionName = emptyText,
            webPublicationDate = emptyText,
            webTitle = sampleText,
            webUrl = emptyText,
            apiUrl = emptyText,
            fields = Fields(
                emptyText,
                emptyText,
                emptyText,
                emptyText
            ),
            isHosted = false,
            pillarId = emptyText,
            pillarName = emptyText,
            isFavorite = false
        ),
        New(
            id = emptyText,
            type = emptyText,
            sectionId = emptyText,
            sectionName = emptyText,
            webPublicationDate = emptyText,
            webTitle = sampleText,
            webUrl = emptyText,
            apiUrl = emptyText,
            fields = Fields(
                emptyText,
                emptyText,
                emptyText,
                emptyText
            ),
            isHosted = false,
            pillarId = emptyText,
            pillarName = emptyText,
            isFavorite = false
        ),
        New(
            id = emptyText,
            type = emptyText,
            sectionId = emptyText,
            sectionName = emptyText,
            webPublicationDate = emptyText,
            webTitle = sampleText,
            webUrl = emptyText,
            apiUrl = emptyText,
            fields = Fields(
                emptyText,
                emptyText,
                emptyText,
                emptyText
            ),
            isHosted = false,
            pillarId = emptyText,
            pillarName = emptyText,
            isFavorite = false
        )
    )
    NewsAppTheme {
        FavoritesScreenContent(
            favoritesList = list,
            deleteFavorite = {},
            navigateToDetail = {}
        )
    }
}
