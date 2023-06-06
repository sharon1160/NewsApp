package com.example.newsapp.view.ui.screens.search

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.newsapp.service.model.Fields
import com.example.newsapp.service.model.New
import com.example.newsapp.view.ui.theme.NewsAppTheme
import com.example.newsapp.view.ui.theme.Roboto
import com.example.newsapp.viewmodel.FavoritesViewModel
import com.example.newsapp.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel,
    favoritesViewModel: FavoritesViewModel,
    navController: NavHostController
) {
    val uiState by searchViewModel.uiState.collectAsState()

    val navigateToDetail = { webUrl: String ->
        navController.navigate("detail/${Uri.encode(webUrl)}")
    }

    NewsAppTheme {
        SearchContent(
            uiState.newsList,
            searchViewModel::searchNew,
            searchViewModel::updateIsFavorite,
            favoritesViewModel::insert,
            favoritesViewModel::delete,
            navigateToDetail
            /*
            detailViewModel::updateDetail,
            uiState.newsList,
            searchViewModel::searchById,*/
        )
    }
}

@Composable
fun SearchContent(
    newsList: MutableList<New>,
    searchNew: (String) -> Unit,
    updateIsFavorite: (New) -> Unit,
    insertFavorite: (New) -> Unit,
    deleteFavorite: (New) -> Unit,
    navigateToDetail: (String) -> Unit
    /*
    updateDetail: (New) -> Unit,
    newsList: MutableList<New>,
    searchByNew: (String) -> Unit,
    onClickItem: () -> Job*/
) {
    Box(
        modifier = Modifier
            .padding(top = 15.dp, end = 2.dp, start = 2.dp)
            .fillMaxSize()
    ) {
        SearchNewBar(searchNew)
        if (newsList.isNotEmpty()) {
            NewsList(
                newsList,
                updateIsFavorite,
                insertFavorite,
                deleteFavorite,
                navigateToDetail
                /*
                updateDetail,
                searchByNew,
                onClickItem*/
            )
        } else {
            Message("Welcome! Do a search")
        }
    }
}

@Composable
fun Message(text: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            fontFamily = Roboto,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Light
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchNewBar(searchNew: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    Scaffold {
        SearchBar(
            modifier = Modifier.fillMaxWidth(),
            query = text,
            onQueryChange = {
                text = it
            },
            onSearch = {
                if (it.isNotEmpty()) {
                    searchNew(it)
                    active = false
                    text = ""
                }
            },
            active = active,
            onActiveChange = {
                active = it
            },
            placeholder = {
                Text(
                    text = "Search",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Light
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search icon"
                )
            },
            trailingIcon = {
                if (active) {
                    Icon(
                        modifier = Modifier.clickable {
                            if (text.isNotEmpty()) {
                                text = ""
                            } else {
                                active = false
                            }
                        },
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close icon"
                    )
                }
            }
        ) {}
    }
}

@Composable
fun NewsList(
    newsList: List<New> = emptyList(),
    updateIsFavorite: (New) -> Unit,
    insertFavorite: (New) -> Unit,
    deleteFavorite: (New) -> Unit,
    navigateToDetail: (String) -> Unit
    /*
    updateDetail: (New) -> Unit,
    searchByNew: (String) -> Unit,
    onClickItem: () -> Job*/
) {
    Box(modifier = Modifier.padding(top = 120.dp, bottom = 50.dp)) {
        LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
            items(items = newsList) { new ->
                ListItem(
                    new,
                    updateIsFavorite,
                    insertFavorite,
                    deleteFavorite,
                    navigateToDetail
                    /*
                    updateDetail,
                    searchByNew,
                    onClickItem*/
                )
            }
        }
    }
}

@Composable
fun ListItem(
    new: New,
    updateIsFavorite: (New) -> Unit,
    insertFavorite: (New) -> Unit,
    deleteFavorite: (New) -> Unit,
    navigateToDetail: (String) -> Unit
    /*
    updateDetail: (New) -> Unit,
    searchByNew: (String) -> Unit,
    onClickItem: () -> Job*/
) {
    Surface(
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier.padding(vertical = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val lineColor = MaterialTheme.colorScheme.inversePrimary
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .drawBehind {
                        drawLine(
                            color = lineColor,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = 2.dp.toPx()
                        )
                    }
                    .clickable {
                        navigateToDetail(new.webUrl)
                    }
            ) {

                var thumbnail: String = new.fields.thumbnail
                if (thumbnail == "N/A") {
                    thumbnail =
                        "https://plantillasdememes.com/img/plantillas/imagen-no-disponible01601774755.jpg"
                }

                AsyncImage(
                    model = thumbnail,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 16.dp, top = 6.dp, bottom = 6.dp)
                        .height(80.dp)
                        .width(80.dp)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )


                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp, end = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = new.webTitle,
                        fontFamily = Roboto,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Light
                    )
                }
                FavoritesButton(
                    new,
                    updateIsFavorite,
                    insertFavorite,
                    deleteFavorite
                )
            }
        }
    }
}

@Composable
fun FavoritesButton(
    new: New,
    updateIsFavorite: (New) -> Unit,
    insertFavorite: (New) -> Unit,
    deleteFavorite: (New) -> Unit
) {
    IconButton(
        modifier = Modifier.padding(end = 16.dp),
        onClick = {
            if (!new.isFavorite) {
                insertFavorite(new)
            } else {
                deleteFavorite(new)
            }
            updateIsFavorite(new)
        }
    ) {
        val icon = if (new.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder
        val tint = if (new.isFavorite) Color.Red else Color.Gray
        Icon(
            imageVector = icon,
            contentDescription = "Favorite Icon",
            tint = tint
        )
    }
}

@Preview
@Composable
fun SearchPreview() {
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
        )
    )
    NewsAppTheme {
        SearchContent(
            newsList = list,
            searchNew = {},
            updateIsFavorite = {},
            insertFavorite = {},
            deleteFavorite = {},
            navigateToDetail = {
                    webUrl: String ->
                    Log.d("TAG", webUrl)
            }
        )
    }
}
