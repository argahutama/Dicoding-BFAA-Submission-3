package com.argahutama.consumerapp.db

import android.database.Cursor
import com.argahutama.consumerapp.db.DbContract.UserFavoriteColumns.Companion.AVATAR
import com.argahutama.consumerapp.db.DbContract.UserFavoriteColumns.Companion.FAVORITE
import com.argahutama.consumerapp.db.DbContract.UserFavoriteColumns.Companion.URL
import com.argahutama.consumerapp.db.DbContract.UserFavoriteColumns.Companion.USERNAME
import com.argahutama.consumerapp.models.FavoriteUser

object MappingHelper {
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<FavoriteUser> {
        val favList = ArrayList<FavoriteUser>()

        cursor?.apply {
            while (moveToNext()) {
                val username =
                    getString(getColumnIndexOrThrow(USERNAME))
                val avatar =
                    getString(getColumnIndexOrThrow(AVATAR))
                val url =
                    getString(getColumnIndexOrThrow(URL))
                val isFav =
                    getString(getColumnIndexOrThrow(FAVORITE))

                favList.add(
                    FavoriteUser(
                        username,
                        avatar,
                        url,
                        isFav
                    )
                )
            }
        }
        return favList
    }

}