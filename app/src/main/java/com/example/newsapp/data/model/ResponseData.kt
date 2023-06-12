package com.example.newsapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ResponseData (
    val status: String,
    val userTier: String,
    val total: Int,
    val startIndex: Int,
    val pageSize: Int,
    val currentPage: Int,
    val pages: Int,
    val orderBy: String,
    val results: List<New>
)
