package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.ui.NewsActivity
import com.example.newsapp.utils.Constants.Companion.KEY_ARTICLE
import com.example.newsapp.viewmodels.NewsViewModel
import kotlinx.android.synthetic.main.fragment_saved_news.*

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {
    lateinit var viewModel: NewsViewModel
    lateinit var savedAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setRecyclerView()
        viewModel.getSavedArticles().observe(viewLifecycleOwner, Observer {
            savedAdapter.differ.submitList(it)
        })
    }

    private fun setRecyclerView() {
        savedAdapter = NewsAdapter()
        rvSavedNews.apply {
            adapter = savedAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        savedAdapter.setOnArticleClickListener { article->
            val bundle = Bundle()
            bundle.putSerializable(KEY_ARTICLE, article)
            findNavController().navigate(R.id.action_savedNewsFragment_to_articleFragment, bundle)
        }
    }
}