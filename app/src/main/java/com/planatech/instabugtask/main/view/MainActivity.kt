package com.planatech.instabugtask.main.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.planatech.instabugtask.R
import com.planatech.instabugtask.databinding.ActivityMainBinding
import com.planatech.instabugtask.main.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private var binding: ActivityMainBinding? = null
    private var isDescending = true

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        handleIntent(intent)

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            this.setOnCloseListener {
                mainViewModel.resetList()
                this.onActionViewCollapsed()
                true
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort -> {
                mainViewModel.sortList(isDescending)
                isDescending = !isDescending
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            mainViewModel.search(query)
        }
    }

}
