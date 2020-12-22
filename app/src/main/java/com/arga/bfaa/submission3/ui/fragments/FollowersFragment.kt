package com.arga.bfaa.submission3.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arga.bfaa.submission3.R
import com.arga.bfaa.submission3.adapters.FollowersAdapter
import com.arga.bfaa.submission3.models.GithubUser
import com.arga.bfaa.submission3.ui.activities.DetailActivity
import com.arga.bfaa.submission3.viewmodels.FollowersViewModel
import kotlinx.android.synthetic.main.fragment_followers.*

class FollowersFragment : Fragment() {
    private lateinit var adapter: FollowersAdapter
    private lateinit var followersViewModel: FollowersViewModel

    companion object {
        const val EXTRA_FOLLOWERS = "extra_followers"
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FollowersAdapter()
        showRecyclerView()

        followersViewModel = ViewModelProvider(this, ViewModelProvider
            .NewInstanceFactory())
            .get(
            FollowersViewModel::class.java)
        if(arguments != null){
            val username = arguments?.getString(EXTRA_FOLLOWERS)
            showLoading(true)
            followersViewModel.setFollowersGithubUser(username.toString())
        }

        followersViewModel.getFollowersGithubUsers()
            .observe(viewLifecycleOwner, { githubUser ->
            if(githubUser != null){
                adapter.setData(githubUser)
                showLoading(false)
            }
        })

        followersViewModel.errorMessage.observe(viewLifecycleOwner, { it ->
            it.getContentIfNotHandled()?.let {
                Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
                showLoading(false)
            }
        })
    }

    private fun showRecyclerView() {
        rv_followers_github_user.layoutManager = LinearLayoutManager(context)
        rv_followers_github_user.adapter = adapter

        adapter.notifyDataSetChanged()

        adapter.setOnItemClickCallback(object : FollowersAdapter.OnItemClickCallback{
            override fun onItemClicked(data: GithubUser) {
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_STATE, data)
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
