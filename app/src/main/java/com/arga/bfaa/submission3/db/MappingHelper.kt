package com.arga.bfaa.submission3.db

import android.database.Cursor
import com.arga.bfaa.submission3.db.DbContract.UserFavoriteColumns.Companion.AVATAR
import com.arga.bfaa.submission3.db.DbContract.UserFavoriteColumns.Companion.FAVORITE
import com.arga.bfaa.submission3.db.DbContract.UserFavoriteColumns.Companion.URL
import com.arga.bfaa.submission3.db.DbContract.UserFavoriteColumns.Companion.USERNAME
import com.arga.bfaa.submission3.models.FavoriteUser

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