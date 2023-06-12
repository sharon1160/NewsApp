package com.example.newsapp.ui.detail

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.newsapp.R
import com.example.newsapp.ui.theme.NewsAppTheme

@Composable
fun DetailScreen(webUrl: String) {
    NewsAppTheme {
        DetailScreenContent(webUrl)
    }
}

@Composable
fun DetailScreenContent(webUrl: String) {
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
fun DetailScreenPreview() {
    val webUrl = stringResource(R.string.web_example)
    NewsAppTheme {
        DetailScreenContent(webUrl)
    }
}
