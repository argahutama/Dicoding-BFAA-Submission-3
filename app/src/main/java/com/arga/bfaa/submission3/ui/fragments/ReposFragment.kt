package com.arga.bfaa.submission3.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arga.bfaa.submission3.R
import com.arga.bfaa.submission3.adapters.ReposAdapter
import com.arga.bfaa.submission3.models.GithubUser
import com.arga.bfaa.submission3.viewmodels.ReposViewModel
import kotlinx.android.synthetic.main.fragment_repos.*

class ReposFragment : Fragment() {
    private lateinit var adapter: ReposAdapter
    private lateinit var reposViewModel: ReposViewModel


    companion object {
        const val EXTRA_REPOS = "EXTRA_REPOS"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_repos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ReposAdapter()
        showRecyclerView()

        reposViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(ReposViewModel::class.java)
        if(arguments != null){
            val username = arguments?.getString(EXTRA_REPOS)
            showLoading(true)
            reposViewModel.setReposGithubUser(username.toString())
        }

        reposViewModel.getReposGithubUsers().observe(viewLifecycleOwner, { githubUser ->
            if (githubUser != null) {
                adapter.setData(githubUser)
                showLoading(false)
            }
        })

        reposViewModel.errorMessage.observe(viewLifecycleOwner, { it ->
            it.getContentIfNotHandled()?.let {
                Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
                showLoading(false)
            }
        })
    }

    private fun showRecyclerView() {
        rv_repos_github_user.layoutManager = LinearLayoutManager(context)
        rv_repos_github_user.adapter = adapter

        adapter.notifyDataSetChanged()

        adapter.setOnItemClickCallback(object : ReposAdapter.OnItemClickCallback {
            override fun onItemClicked(data: GithubUser) {
                val url = data.repositoryUrl
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

}
