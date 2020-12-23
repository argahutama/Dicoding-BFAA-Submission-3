package com.arga.bfaa.submission3.adapters

import android.content.Context
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.arga.bfaa.submission3.R
import com.arga.bfaa.submission3.ui.fragments.FollowersFragment
import com.arga.bfaa.submission3.ui.fragments.FollowingFragment
import com.arga.bfaa.submission3.ui.fragments.ReposFragment

class SectionsPagerAdapter(
    private val context: Context,
    fm : FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var username : String? = "username"

    @StringRes
    val tabTitle = intArrayOf(
        R.string.tab_repos,
        R.string.followers,
        R.string.following
    )

    fun setData(usernameGithub : String){
        username = usernameGithub
    }

    private fun getData(): String? {
        return username
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = ReposFragment()
                val bundle = Bundle()
                bundle.putString(ReposFragment.EXTRA_REPOS, getData())
                fragment.arguments = bundle
            }
            1 -> {
                fragment = FollowersFragment()
                val bundle = Bundle()
                bundle.putString(FollowersFragment.EXTRA_FOLLOWERS, getData())
                fragment.arguments = bundle
            }
            2 -> {
                fragment = FollowingFragment()
                val bundle = Bundle()
                bundle.putString(FollowingFragment.EXTRA_FOLLOWING, getData())
                fragment.arguments = bundle
            }

        }
        return fragment as Fragment
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(tabTitle[position])
    }

    override fun getCount(): Int {
        return 3
    }

}
