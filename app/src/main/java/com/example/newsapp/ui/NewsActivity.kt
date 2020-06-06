package com.example.newsapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsapp.viewmodels.NewsViewModelFactory
import com.example.newsapp.R
import com.example.newsapp.data.db.ArticleDatabase
import com.example.newsapp.repositories.ArticleRepository
import com.example.newsapp.viewmodels.NewsViewModel
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity() {
    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val repo = ArticleRepository(
            ArticleDatabase(this)
        )
        viewModel = ViewModelProvider(this,
            NewsViewModelFactory(repo)
        ).get(NewsViewModel::class.java)

        bottomNavigationView.setupWithNavController(navHost.findNavController())
    }
}