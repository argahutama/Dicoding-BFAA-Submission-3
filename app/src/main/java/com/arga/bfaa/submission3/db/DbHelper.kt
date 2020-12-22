package com.arga.bfaa.submission3.db

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.arga.bfaa.submission3.db.DbContract.UserFavoriteColumns.Companion.AVATAR
import com.arga.bfaa.submission3.db.DbContract.UserFavoriteColumns.Companion.FAVORITE
import com.arga.bfaa.submission3.db.DbContract.UserFavoriteColumns.Companion.TABLE_NAME
import com.arga.bfaa.submission3.db.DbContract.UserFavoriteColumns.Companion.URL
import com.arga.bfaa.submission3.db.DbContract.UserFavoriteColumns.Companion.USERNAME

internal class DbHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME, null,
    DATABASE_VERSION
) {
    companion object {
        private const val DATABASE_NAME = "final_submission.db"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE = "CREATE TABLE $TABLE_NAME" +
                " ($USERNAME TEXT NOT NULL," +
                " $AVATAR TEXT NOT NULL," +
                " $URL TEXT NOT NULL," +
                " $FAVORITE TEXT NOT NULL)"
    }

    @SuppressLint("SQLiteString")
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}