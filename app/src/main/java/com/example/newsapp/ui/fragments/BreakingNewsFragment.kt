package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.ui.NewsActivity
import com.example.newsapp.utils.Constants.Companion.KEY_ARTICLE
import com.example.newsapp.utils.Constants.Companion.PAGE_SIZE_QUERY
import com.example.newsapp.utils.Resource
import com.example.newsapp.viewmodels.NewsViewModel
import kotlinx.android.synthetic.main.fragment_breaking_news.*

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    var isScrolling = false
    var isLoading = false
    var isLastPage = false

    var myScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleArticlePosition = layoutManager.findFirstVisibleItemPosition()
            val totalArticlesCount = layoutManager.itemCount
            val visibleArticlesCount = layoutManager.childCount

            val isNotAtLastPageAndNotLoading = !isLastPage && !isLoading
            val isAtLastItem =
                firstVisibleArticlePosition + visibleArticlesCount >= totalArticlesCount
            val isNotAtBeginning = firstVisibleArticlePosition >= 0
            val isTotalMoreThanVisible = totalArticlesCount >= PAGE_SIZE_QUERY
            val shouldPaginate =
                isNotAtLastPageAndNotLoading && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            Log.d("dupa", shouldPaginate.toString())
            if (shouldPaginate) {
                viewModel.getBreakingNews("us")
                isScrolling = false
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setRecyclerView()

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Success -> {
                    hideProgressBar()
                    resource.data?.let {
                        //toList() so that DiffUtil can distinguish the differences after adding more data
                        newsAdapter.differ.submitList(it.articles.toList())
                        //DUE TO INT ROUNDING AND LAST EMPTY PAGE
                        val totalPages = it.totalResults / PAGE_SIZE_QUERY + 2
                        isLastPage = (totalPages == viewModel.breakingNewsPage)
                    }

                }
                is Resource.Error -> {
                    hideProgressBar()
                    Log.e("dupa", resource.message!!)
                }
                is Resource.Loading -> showProgressBar()
            }

        })
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun setRecyclerView() {
        newsAdapter = NewsAdapter()
        rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.myScrollListener)
        }
        newsAdapter.setOnArticleClickListener { article ->
            val bundle = Bundle()
            bundle.putSerializable(KEY_ARTICLE, article)
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }
    }
}