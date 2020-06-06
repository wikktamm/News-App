package com.example.newsapp.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.models.Article
import com.example.newsapp.data.models.NewsResponse
import com.example.newsapp.repositories.ArticleRepository
import com.example.newsapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(var repository: ArticleRepository) : ViewModel() {
    var breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse : NewsResponse? = null

    var searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String) {
        Log.d("call","call")
        viewModelScope.launch {
            breakingNews.postValue(Resource.Loading())
            val response = repository.getBreakingNews(countryCode, breakingNewsPage)
            breakingNews.postValue(handleGetBreakingNews(response))
        }
    }

    fun getSearchNews(stringQuery: String, countryCode: String = "us") {
        viewModelScope.launch {
            searchNews.postValue(Resource.Loading())
            val response = repository.searchNews(stringQuery, countryCode, searchNewsPage)
            searchNews.postValue(handleSearchNews(response))
        }
    }

    private fun handleGetBreakingNews(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { newsResponse->
                breakingNewsPage++
                if(breakingNewsResponse==null){
                    breakingNewsResponse = newsResponse
                }
                else{
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = newsResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: newsResponse)
            }
        }
        //todo?
        return Resource.Error(response.body(), response.message())
    }

    private fun handleSearchNews(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        //todo?
        return Resource.Error(response.body(), response.message())
    }

    fun saveArticle(article: Article){
        viewModelScope.launch {
            repository.saveArticle(article)
        }
    }
    fun getSavedArticles() = repository.getSavedArticles()
}