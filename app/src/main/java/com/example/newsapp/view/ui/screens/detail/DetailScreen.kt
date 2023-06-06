package com.example.newsapp.view.ui.screens.detail

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.newsapp.view.ui.theme.NewsAppTheme

@Composable
fun DetailScreen(webUrl: String) {
    NewsAppTheme {
        DetailContent(webUrl)
    }
}

@Composable
fun DetailContent(webUrl: String) {
    AndroidView(factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            webViewClient = WebViewClient()
            loadUrl(webUrl)
        }
    }, update = {
        it.loadUrl(webUrl)
    })
}

@Preview
@Composable
fun DetailPreview() {
    val webUrl = "https://www.google.com"
    NewsAppTheme {
        DetailContent(webUrl)
    }
}
