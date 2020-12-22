package com.argahutama.consumerapp.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.argahutama.consumerapp.R
import com.argahutama.consumerapp.models.FavoriteUser
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_favorite_user.view.*

class FavoriteAdapter(private val activity: Activity) :
    RecyclerView.Adapter<FavoriteAdapter.ListViewHolder>() {

    var listFavorite = ArrayList<FavoriteUser>()
        set(listFavourite) {
            if (listFavourite.size > 0) {
                this.listFavorite.clear()
            }
            this.listFavorite.addAll(listFavourite)
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
        return listFavorite.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }
}