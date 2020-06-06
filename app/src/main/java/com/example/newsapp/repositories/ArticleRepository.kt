package com.example.newsapp.repositories

import com.example.newsapp.data.api.RetrofitInstance
import com.example.newsapp.data.db.ArticleDatabase

class ArticleRepository(private var database: ArticleDatabase) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(query: String, countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(query, countryCode, pageNumber)
}