package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.ui.NewsActivity
import com.example.newsapp.utils.Constants.Companion.KEY_ARTICLE
import com.example.newsapp.viewmodels.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_saved_news.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

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

        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = savedAdapter.differ.currentList[position]
                MainScope().launch {
                    viewModel.repository.deleteArticle(article)
                    Snackbar.make(view, "Chosen article has been deleted", Snackbar.LENGTH_SHORT).setAction("Undo", object : View.OnClickListener{
                        override fun onClick(p0: View?) {
                            viewModel.saveArticle(article)
                        }
                    }).show()
                }
            }
        }
        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(rvSavedNews)
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