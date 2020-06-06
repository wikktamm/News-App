package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.ui.NewsActivity
import com.example.newsapp.utils.Constants.Companion.KEY_ARTICLE
import com.example.newsapp.utils.Resource
import com.example.newsapp.viewmodels.NewsViewModel
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {
    lateinit var viewModel: NewsViewModel
    lateinit var searchAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel

        viewModel.searchNews.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Success -> {
                    hideProgressBar()
                    resource.data?.let {
                        searchAdapter.differ.submitList(it.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    Log.e("dupa", resource.message!!)
                }
                is Resource.Loading -> showProgressBar()
            }

        })
        setRecyclerView()
        var job: Job? = null
        etSearch.addTextChangedListener {
            job?.cancel()
            job = MainScope().launch {
                delay(600L)
                it?.let {
                    if(it.toString().isNotEmpty()){
                        viewModel.getSearchNews(it.toString())
                    }
                }
            }
        }
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setRecyclerView() {
        searchAdapter = NewsAdapter()
        rvSearchNews.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        searchAdapter.setOnArticleClickListener { article->
            val bundle = Bundle()
            bundle.putSerializable(KEY_ARTICLE, article)
            findNavController().navigate(R.id.action_searchNewsFragment_to_articleFragment, bundle)
        }
    }
}