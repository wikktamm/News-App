package com.example.newsapp.repositories

import com.example.newsapp.data.api.RetrofitInstance
import com.example.newsapp.data.db.ArticleDatabase
import com.example.newsapp.data.models.Article

class ArticleRepository(private var database: ArticleDatabase) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(query: String, countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(query, countryCode, pageNumber)

    suspend fun saveArticle(article: Article) = database.getArticleDao().upsert(article)
    suspend fun deleteArticle(article: Article) = database.getArticleDao().delete(article)
    fun getSavedArticles() = database.getArticleDao().getSavedArticles()
}