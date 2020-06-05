package com.example.newsapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsapp.data.models.Article

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article)
    @Delete
    suspend fun delete(article:Article)
    @Query("SELECT * FROM articles")
    fun getSavedArticles():LiveData<List<Article>>
}