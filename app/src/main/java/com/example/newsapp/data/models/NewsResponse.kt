package com.example.newsapp.data.models

import com.example.newsapp.data.models.Article


data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)