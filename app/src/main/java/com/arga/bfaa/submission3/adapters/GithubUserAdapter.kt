package com.arga.bfaa.submission3.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arga.bfaa.submission3.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.arga.bfaa.submission3.models.GithubUser
import kotlinx.android.synthetic.main.item_row_github_user.view.*


class GithubUserAdapter : RecyclerView.Adapter<GithubUserAdapter.GithubViewHolder>() {
    private val listGithubUser = ArrayList<GithubUser>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setData(items: ArrayList<GithubUser>) {
        listGithubUser.clear()
        listGithubUser.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup : ViewGroup, viewType: Int): GithubViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_row_github_user, viewGroup, false)
        return GithubViewHolder(view)
    }

    override fun getItemCount(): Int = listGithubUser.size

    override fun onBindViewHolder(holder: GithubViewHolder, position: Int) {
        holder.bind(listGithubUser[position])
    }

    inner class GithubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(githubUser: GithubUser){
            with(itemView){
                Glide.with(itemView.context)
                    .load(githubUser.avatar)
                    .apply(RequestOptions().override(250, 250))
                    .into(image_avatar)
                tv_username.text = githubUser.username
                tv_url.text = githubUser.url
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