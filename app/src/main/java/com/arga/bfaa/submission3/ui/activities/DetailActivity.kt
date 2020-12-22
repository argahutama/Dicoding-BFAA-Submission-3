package com.arga.bfaa.submission3.ui.activities

import android.content.ContentValues
import android.database.Cursor
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.arga.bfaa.submission3.R
import com.arga.bfaa.submission3.adapters.SectionsPagerAdapter
import com.arga.bfaa.submission3.db.DbContract.UserFavoriteColumns.Companion.AVATAR
import com.arga.bfaa.submission3.db.DbContract.UserFavoriteColumns.Companion.CONTENT_URI
import com.arga.bfaa.submission3.db.DbContract.UserFavoriteColumns.Companion.FAVORITE
import com.arga.bfaa.submission3.db.DbContract.UserFavoriteColumns.Companion.URL
import com.arga.bfaa.submission3.db.DbContract.UserFavoriteColumns.Companion.USERNAME
import com.arga.bfaa.submission3.db.QueryHelper
import com.arga.bfaa.submission3.models.GithubUser
import com.arga.bfaa.submission3.ui.fragments.FollowersFragment
import com.arga.bfaa.submission3.ui.fragments.FollowingFragment
import com.arga.bfaa.submission3.ui.fragments.ReposFragment
import com.arga.bfaa.submission3.viewmodels.DetailViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var dbHelper: QueryHelper
    private var statusFavorite = false

    companion object {
        const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val detailGithubUser = intent.getParcelableExtra(EXTRA_STATE) as GithubUser?

        dbHelper = QueryHelper.getInstance(applicationContext)
        dbHelper.open()

        val usernameVal = intent.getParcelableExtra(EXTRA_STATE) as GithubUser?
        val cursor: Cursor = dbHelper.queryByUsername(usernameVal?.username.toString())
        if (cursor.moveToNext()) {
            statusFavorite = true
            setStatusFavorite(true)
        }

        Glide.with(this)
            .load(detailGithubUser!!.avatar)
            .into(image_avatar)
        if (detailGithubUser != null) {
            tv_username.text = detailGithubUser.username
        }

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)

        if (detailGithubUser != null) {
            detailViewModel.setDetailGithubUser(detailGithubUser.username)
        }
        detailViewModel.getDetailGithubUsers().observe(this, { githubUser ->
            if(githubUser != null) {
                if(githubUser.name == "null"){
                    tv_name.text = "-"
                } else {
                    tv_name.text = githubUser.name
                }
                if(githubUser.location.toString() == "null"){
                    tv_location.text = "-"
                } else {
                    tv_location.text = githubUser.location
                }
                tv_repo.text = githubUser.repository.toString()
                tv_detail_followers.text = githubUser.followers.toString()
                tv_detail_following.text = githubUser.following.toString()
            }
        })

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        if (detailGithubUser != null) {
            sectionsPagerAdapter.setData(detailGithubUser.username)
        }
        view_pager.adapter = sectionsPagerAdapter
        tabs_follow.setupWithViewPager(view_pager)

        val bundle = Bundle()

        val followersFragment = FollowersFragment()
        if (detailGithubUser != null) {
            bundle.putString(FollowersFragment.EXTRA_FOLLOWERS, detailGithubUser.username)
        }
        followersFragment.arguments = bundle

        val followingFragment = FollowingFragment()
        if (detailGithubUser != null) {
            bundle.putString(FollowingFragment.EXTRA_FOLLOWING, detailGithubUser.username)
        }
        followingFragment.arguments = bundle

        val reposFragment = ReposFragment()
        if (detailGithubUser != null) {
            bundle.putString(ReposFragment.EXTRA_REPOS, detailGithubUser.username)
        }
        reposFragment.arguments = bundle

        fab_favorite.setOnClickListener {
            if (statusFavorite) {
                val idUser = detailGithubUser!!.username
                dbHelper.deleteById(idUser)
                Toast.makeText(this, "Data Deleted from Favorite", Toast.LENGTH_SHORT).show()
                setStatusFavorite(false)
                statusFavorite = false
            } else {
                val values = ContentValues()
                values.put(USERNAME, detailGithubUser!!.username)
                values.put(AVATAR, detailGithubUser.avatar)
                values.put(URL, detailGithubUser.url)
                values.put(FAVORITE, "1")

                statusFavorite = true
                    contentResolver.insert(CONTENT_URI, values)
                Toast.makeText(this, "Data Added to Favorite", Toast.LENGTH_SHORT).show()
                setStatusFavorite(true)
            }
        }

    }

    private fun setStatusFavorite(status: Boolean) {
        if (status) {
            fab_favorite.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            fab_favorite.setImageResource(R.drawable.ic_baseline_unfavorite_24)
        }
    }
}
