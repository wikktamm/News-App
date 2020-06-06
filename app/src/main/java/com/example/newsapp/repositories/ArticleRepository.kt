package com.example.newsapp.repositories

import com.example.newsapp.data.api.RetrofitInstance
import com.example.newsapp.data.db.ArticleDatabase
import com.example.newsapp.data.models.NewsResponse
import retrofit2.Response

class ArticleRepository(private var database: ArticleDatabase) {
    suspend fun getBreakingNews(countryCode:String, pageNumber:Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

}