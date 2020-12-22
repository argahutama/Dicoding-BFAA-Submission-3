package com.arga.bfaa.submission3.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.arga.bfaa.submission3.db.DbContract.Companion.AUTHORITY
import com.arga.bfaa.submission3.db.DbContract.UserFavoriteColumns.Companion.CONTENT_URI
import com.arga.bfaa.submission3.db.DbContract.UserFavoriteColumns.Companion.TABLE_NAME
import com.arga.bfaa.submission3.db.QueryHelper

class FavProvider : ContentProvider() {

    companion object {
        private const val FAV = 1
        private const val FAV_ID = 2
        private lateinit var dbHelper: QueryHelper
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAV)
            sUriMatcher.addURI(
                AUTHORITY, "${TABLE_NAME}/#",
                FAV_ID
            )
        }
    }


    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val insert: Long = when (FAV) {
            sUriMatcher.match(uri) -> dbHelper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$insert")
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return when (sUriMatcher.match(uri)) {
            FAV -> dbHelper.queryAll()
            FAV_ID -> dbHelper.queryByUsername(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun onCreate(): Boolean {
        dbHelper = QueryHelper.getInstance(context as Context)
        dbHelper.open()
        return true
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val updated: Int = when (FAV_ID) {
            sUriMatcher.match(uri) -> dbHelper.update(
                uri.lastPathSegment.toString(),
                values!!
            )
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val deleted: Int = when (FAV_ID) {
            sUriMatcher.match(uri) -> dbHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun getType(uri: Uri): String? {
        return null
    }
}