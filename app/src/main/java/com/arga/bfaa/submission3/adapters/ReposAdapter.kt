package com.arga.bfaa.submission3.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arga.bfaa.submission3.R
import com.arga.bfaa.submission3.models.GithubUser
import kotlinx.android.synthetic.main.item_repo_github_user.view.*

class ReposAdapter : RecyclerView.Adapter<ReposAdapter.ReposViewHolder>() {
    private val listReposGithubUser = ArrayList<GithubUser>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setData(items: ArrayList<GithubUser>) {
        listReposGithubUser.clear()
        listReposGithubUser.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup : ViewGroup, viewType: Int): ReposViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_repo_github_user, viewGroup, false)
        return ReposViewHolder(view)
    }

    override fun getItemCount(): Int = listReposGithubUser.size

    override fun onBindViewHolder(holder: ReposViewHolder, position: Int) {
        holder.bind(listReposGithubUser[position])
    }

    inner class ReposViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(githubUser: GithubUser) {
            with(itemView) {
                tv_repo_name.text = githubUser.repositoryName
                tv_repo_url.text = githubUser.repositoryUrl
                if (githubUser.repositoryLanguage == "null") {
                    tv_repo_language.text = ""
                } else {
                    tv_repo_language.text = githubUser.repositoryLanguage
                }
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