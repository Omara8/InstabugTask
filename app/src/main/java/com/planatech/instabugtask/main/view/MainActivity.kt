package com.planatech.instabugtask.main.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.planatech.instabugtask.databinding.ActivityMainBinding
import com.planatech.instabugtask.main.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setUpViewModel()
    }

    private fun setUpViewModel() {
        mainViewModel.loadWebsite("https://instabug.com")

        mainViewModel.frequency.observe(this, {
            displayList(it)
        })
        mainViewModel.isLoading.observe(this, {
            when (it) {
                true -> {
                    binding?.mainContent?.showLoading()
                }
                false -> {
                    binding?.mainContent?.showContent()
                }
            }
        })
    }

    private fun displayList(response: MutableMap<String, Int>?) {
        val words: List<Pair<String, Int>>? = response?.toList()
        val wordsAdapter = WordsAdapter(words)
        binding?.recyclerView?.adapter = wordsAdapter
    }

}