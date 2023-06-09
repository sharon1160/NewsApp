package com.example.newsapp.ui.search

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
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
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.newsapp.R
import com.example.newsapp.data.datastore.StoreFilters
import com.example.newsapp.data.model.New
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.newsapp.ui.theme.Roboto
import com.example.newsapp.ui.favorites.FavoritesViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = koinViewModel(),
    favoritesViewModel: FavoritesViewModel = koinViewModel(),
    navController: NavHostController
) {
    val resultedList = searchViewModel.resultedList.collectAsLazyPagingItems()
    val uiState by searchViewModel.uiState.collectAsState()
    val detailBasePath = stringResource(R.string.detail_base_path)

    val navigateToDetail = { webUrl: String ->
        navController.navigate("${detailBasePath}${Uri.encode(webUrl)}")
    }

    NewsAppTheme {
        SearchScreenContent(
            uiState.query,
            searchViewModel::updateQuery,
            resultedList,
            searchViewModel::searchNew,
            favoritesViewModel::insert,
            favoritesViewModel::delete,
            navigateToDetail
        )
    }
}

@Composable
fun SearchScreenContent(
    query: String,
    updateQuery: (String) -> Unit,
    newsList: LazyPagingItems<New>?,
    searchNew: (String, String) -> Unit,
    insertFavorite: (New) -> Unit,
    deleteFavorite: (New) -> Unit,
    navigateToDetail: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(top = 0.dp, end = 2.dp, start = 2.dp)
            .fillMaxSize()
    ) {
        SearchNewBar(searchNew, query, updateQuery)
        Filters(query, searchNew)
        newsList?.let {
            if (newsList.itemCount > 0) {
                NewsList(
                    newsList,
                    insertFavorite,
                    deleteFavorite,
                    navigateToDetail
                )
            } else {
                Message(stringResource(R.string.welcome_message))
            }
        }
        if (newsList == null) {
            Message(stringResource(R.string.welcome_message))
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
fun SearchNewBar(
    searchNew: (String, String) -> Unit,
    query: String,
    updateQuery: (String) -> Unit
) {
    val datastore: StoreFilters = koinInject()
    val defaultFilter = stringResource(R.string.default_filter)
    val savedFilter =
        datastore.getDatastoreFilter.collectAsState(initial = defaultFilter)

    var active by remember { mutableStateOf(false) }

    Scaffold {
        SearchBar(
            modifier = Modifier.fillMaxWidth(),
            query = query,
            onQueryChange = {
                updateQuery(it)
            },
            onSearch = {
                if (it.isNotEmpty()) {
                    searchNew(it, savedFilter.value ?: defaultFilter)
                    active = false
                }
            },
            active = active,
            onActiveChange = {
                active = it
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.search_placeholder),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Light
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search_content_description)
                )
            },
            trailingIcon = {
                if (active) {
                    val emptyText = stringResource(R.string.empty_text)
                    Icon(
                        modifier = Modifier.clickable {
                            if (query.isNotEmpty()) {
                                updateQuery(emptyText)
                            } else {
                                active = false
                            }
                        },
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.close_icon_content_description)
                    )
                }
            }
        ) {}
    }
}

@Composable
fun Filters(query: String, searchNew: (String, String) -> Unit) {
    val scope = rememberCoroutineScope()
    val datastore: StoreFilters = koinInject()
    val savedFilter =
        datastore.getDatastoreFilter.collectAsState(initial = stringResource(R.string.default_filter))
    val filtersList = listOf(
        stringResource(R.string.default_filter),
        stringResource(R.string.newest_filter),
        stringResource(R.string.oldest_filter)
    )
    var selected = savedFilter.value ?: stringResource(R.string.default_filter)

    Row(
        modifier = Modifier
            .padding(top = 75.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
    ) {
        filtersList.forEach {
            FilterChip(
                title = it,
                selected = selected,
                onSelected = { filter ->
                    selected = filter
                    if (query.isNotEmpty()) {
                        searchNew(query, selected)
                    }
                    scope.launch {
                        datastore.saveDatastoreFilter(filter)
                    }
                }
            )
        }
    }
}

@Composable
fun FilterChip(title: String, selected: String, onSelected: (String) -> Unit) {

    val isSelected = title == selected
    val background =
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer
    val contentColor = if (isSelected) Color.White else Color.Black

    Box(modifier = Modifier
        .padding(end = 10.dp)
        .height(35.dp)
        .clip(CircleShape)
        .background(background)
        .clickable(
            onClick = { onSelected(title) }
        )
    ) {
        Row(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            AnimatedVisibility(visible = isSelected) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = stringResource(R.string.check_icon_content_decription),
                    tint = Color.White
                )
            }
            Text(
                text = title,
                color = contentColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                style = MaterialTheme.typography.bodySmall,
                fontFamily = Roboto
            )
        }
    }
}

@Composable
fun NewsList(
    newsList: LazyPagingItems<New>,
    insertFavorite: (New) -> Unit,
    deleteFavorite: (New) -> Unit,
    navigateToDetail: (String) -> Unit
) {
    Box(modifier = Modifier.padding(top = 120.dp, bottom = 50.dp)) {
        LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
            itemsIndexed(items = newsList) { _, new ->
                new?.let {
                    ListItem(
                        it,
                        insertFavorite,
                        deleteFavorite,
                        navigateToDetail
                    )
                }
            }
        }
    }
}

@Composable
fun ListItem(
    new: New,
    insertFavorite: (New) -> Unit,
    deleteFavorite: (New) -> Unit,
    navigateToDetail: (String) -> Unit
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

                AsyncImage(
                    model = new.fields.thumbnail,
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
    insertFavorite: (New) -> Unit,
    deleteFavorite: (New) -> Unit
) {
    var isFavorite = new.isFavorite

    IconButton(
        modifier = Modifier.padding(end = 16.dp),
        onClick = {
            if (!isFavorite) {
                insertFavorite(new)
            } else {
                deleteFavorite(new)
            }
            isFavorite = !isFavorite
        }
    ) {
        val icon = if (isFavorite) {
            Icons.Default.Favorite
        } else {
            Icons.Default.FavoriteBorder
        }
        val tint = if (isFavorite) Color.Red else Color.Gray
        Icon(
            imageVector = icon,
            contentDescription = stringResource(R.string.favorite_icon_content_description),
            tint = tint
        )
    }
}


@Preview
@Composable
fun SearchScreenPreview() {
    NewsAppTheme {
        SearchScreenContent(
            query = stringResource(R.string.empty_text),
            updateQuery = {},
            newsList = null,
            searchNew = { _, _ -> },
            insertFavorite = {},
            deleteFavorite = {},
            navigateToDetail = {}
        )
    }
}
