package com.example.newsapp.view.ui.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.newsapp.view.ui.theme.NewsAppTheme
import com.example.newsapp.view.ui.theme.Roboto

@Composable
fun DetailScreen(webTitle: String, thumbnail: String, bodyText: String) {
    NewsAppTheme {
        DetailContent(webTitle, thumbnail, bodyText)
    }
}

@Composable
fun DetailContent(webTitle: String, thumbnail: String, bodyText: String) {
    Surface {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.4f)) {
                var thumbnailVar: String = thumbnail
                if (thumbnailVar == "N/A") {
                    thumbnailVar =
                        "https://plantillasdememes.com/img/plantillas/imagen-no-disponible01601774755.jpg"
                }

                AsyncImage(
                    model = thumbnailVar,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = webTitle,
                    fontFamily = Roboto,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = bodyText,
                    fontFamily = Roboto,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}

@Preview
@Composable
fun DetailPreview() {
    val webTitle = "title2"
    val thumbnail = "thumbnail"
    val bodyText = "bodyText"
    NewsAppTheme {
        DetailContent(
            webTitle, thumbnail, bodyText
        )
    }
}