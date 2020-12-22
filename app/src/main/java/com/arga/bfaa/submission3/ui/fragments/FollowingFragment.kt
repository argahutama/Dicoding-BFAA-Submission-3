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
import com.arga.bfaa.submission3.adapters.FollowingAdapter
import com.arga.bfaa.submission3.models.GithubUser
import com.arga.bfaa.submission3.ui.activities.DetailActivity
import com.arga.bfaa.submission3.viewmodels.FollowingViewModel
import kotlinx.android.synthetic.main.fragment_following.*

class FollowingFragment : Fragment() {
    private lateinit var adapter: FollowingAdapter
    private lateinit var followingViewModel: FollowingViewModel

    companion object {
        const val EXTRA_FOLLOWING = "extra_following"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FollowingAdapter()
        showRecyclerView()

        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FollowingViewModel::class.java)
        if(arguments != null){
            val username = arguments?.getString(EXTRA_FOLLOWING)
            showLoading(true)
            followingViewModel.setFollowingGithubUser(username.toString())
        }

        followingViewModel.getFollowingGithubUsers()
            .observe(viewLifecycleOwner, { githubUser ->
            if(githubUser != null){
                adapter.setData(githubUser)
                showLoading(false)
            }
        })

        followingViewModel.errorMessage.observe(viewLifecycleOwner, { it ->
            it.getContentIfNotHandled()?.let {
                Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
                showLoading(false)
            }
        })
    }

    private fun showRecyclerView() {
        rv_following_github_user.layoutManager = LinearLayoutManager(context)
        rv_following_github_user.adapter = adapter

        adapter.notifyDataSetChanged()

        adapter.setOnItemClickCallback(object : FollowingAdapter.OnItemClickCallback {
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
