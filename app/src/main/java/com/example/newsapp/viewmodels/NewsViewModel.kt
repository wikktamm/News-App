package com.example.newsapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.models.NewsResponse
import com.example.newsapp.repositories.ArticleRepository
import com.example.newsapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(var repository: ArticleRepository) : ViewModel() {
    var breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1

    var searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1


    init{
        getBreakingNews("us")
    }

    private fun getBreakingNews(countryCode: String) {
        viewModelScope.launch {
            breakingNews.postValue(Resource.Loading())
            val response = repository.getBreakingNews(countryCode, breakingNewsPage)
            breakingNews.postValue(handleGetBreakingNews(response))
        }
    }
    fun getSearchNews(stringQuery:String, countryCode: String = "us") {
        viewModelScope.launch {
            searchNews.postValue(Resource.Loading())
            val response = repository.searchNews(stringQuery, countryCode, searchNewsPage)
            searchNews.postValue(handleSearchNews(response))
        }
    }

    private fun handleGetBreakingNews(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
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
}