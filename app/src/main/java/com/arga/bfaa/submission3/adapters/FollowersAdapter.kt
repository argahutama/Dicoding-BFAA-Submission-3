package com.arga.bfaa.submission3.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arga.bfaa.submission3.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.arga.bfaa.submission3.models.GithubUser
import kotlinx.android.synthetic.main.item_follow_github_user.view.*

class FollowersAdapter : RecyclerView.Adapter<FollowersAdapter.FollowersViewHolder>() {
    private val listFollowersGithubUser = ArrayList<GithubUser>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setData(items: ArrayList<GithubUser>) {
        listFollowersGithubUser.clear()
        listFollowersGithubUser.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup : ViewGroup, viewType: Int): FollowersViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_follow_github_user, viewGroup, false)
        return FollowersViewHolder(view)
    }

    override fun getItemCount(): Int = listFollowersGithubUser.size

    override fun onBindViewHolder(holder: FollowersViewHolder, position: Int) {
        holder.bind(listFollowersGithubUser[position])
    }

    inner class FollowersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(githubUser: GithubUser){
            with(itemView){
                Glide.with(itemView.context)
                    .load(githubUser.avatar)
                    .apply(RequestOptions().override(80, 80))
                    .into(image_avatar)
                tv_username.text = githubUser.username
                tv_link.text = githubUser.url
                itemView.setOnClickListener {
                    onItemClickCallback.onItemClicked(githubUser)
                }
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: GithubUser)
    }
}