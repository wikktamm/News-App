package com.example.newsapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.newsapp.data.db.ArticleRepository

class NewsViewModel(var repository:ArticleRepository) : ViewModel() {

}