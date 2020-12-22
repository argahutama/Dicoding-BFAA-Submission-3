package com.arga.bfaa.submission3.ui.activities

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arga.bfaa.submission3.R
import com.arga.bfaa.submission3.adapters.GithubUserAdapter
import com.arga.bfaa.submission3.models.GithubUser
import com.arga.bfaa.submission3.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private lateinit var adapter: GithubUserAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportActionBar?.title = resources.getString(R.string.app_bar_text)

        adapter = GithubUserAdapter()
        adapter.notifyDataSetChanged()

        showRecyclerList()

        mainViewModel = ViewModelProvider(
            this, ViewModelProvider
                .NewInstanceFactory()
        )
            .get(MainViewModel::class.java)

        mainViewModel.getSearchGithubUsers().observe(this, { githubUser ->
            if (githubUser != null) {
                adapter.setData(githubUser)
                showLoading(false)
                empty_user.visibility = View.GONE
            }
        })

        mainViewModel.errorMessage.observe(this, { it ->
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                showLoading(false)
            }
        })
    }

    private fun showRecyclerList() {
        rv_github_users.layoutManager = LinearLayoutManager(this)
        rv_github_users.adapter = adapter

        adapter.setOnItemClickCallback(object : GithubUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: GithubUser) {
                val intent = Intent(this@HomeActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_STATE, data)
                startActivity(intent)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.btn_search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty()) {
                    return true
                } else {
                    showLoading(true)
                    mainViewModel.setSearchGithubUsers(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.btn_fav) {
            val intent = Intent(this, FavoriteActivity::class.java)
            startActivity(intent)
        }
        if (item.itemId == R.id.btn_setting) {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

}