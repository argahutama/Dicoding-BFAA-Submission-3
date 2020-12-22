package com.arga.bfaa.submission3.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arga.bfaa.submission3.R
import com.arga.bfaa.submission3.models.FavoriteUser
import com.arga.bfaa.submission3.models.GithubUser
import com.arga.bfaa.submission3.ui.activities.DetailActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_favorite_user.view.*

class FavoriteAdapter(private val activity: Activity) :
    RecyclerView.Adapter<FavoriteAdapter.ListViewHolder>() {

    var listFavoriteUser = ArrayList<FavoriteUser>()
        set(listFavourite) {
            if (listFavourite.size > 0) {
                this.listFavoriteUser.clear()
            }
            this.listFavoriteUser.addAll(listFavourite)
            notifyDataSetChanged()
        }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("StringFormatInvalid")
        fun bind(favorite: FavoriteUser) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(favorite.avatar)
                    .apply(RequestOptions().override(100, 100))
                    .into(image_avatar)
                tv_username.text = favorite.username
                tv_url.text = favorite.url
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_favorite_user, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return listFavoriteUser.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listFavoriteUser[position])

        val data = listFavoriteUser[position]
        holder.itemView.setOnClickListener {
            val dataUser = GithubUser(
                data.username,
                null,
                data.avatar,
                data.url
            )
            val mIntent = Intent(it.context, DetailActivity::class.java)
            mIntent.putExtra(DetailActivity.EXTRA_STATE, dataUser)
            it.context.startActivity(mIntent)
            (it.context as Activity).finish()
        }
    }
}