package com.argahutama.consumerapp.db

import android.net.Uri
import android.provider.BaseColumns

internal class DbContract {

    companion object {
        const val AUTHORITY = "com.arga.bfaa.submission3"
        const val SCHEME = "content"
    }

    internal class UserFavoriteColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite_user"
            const val USERNAME = "username"
            const val AVATAR = "avatar"
            const val URL = "url"
            const val FAVORITE = "is_fav"

            val CONTENT_URI: Uri = Uri.Builder()
                .scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}