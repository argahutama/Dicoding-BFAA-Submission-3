package com.arga.bfaa.submission3.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arga.bfaa.submission3.R
import android.database.ContentObserver
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.arga.bfaa.submission3.adapters.FavoriteAdapter
import com.arga.bfaa.submission3.db.DbContract.UserFavoriteColumns.Companion.CONTENT_URI
import com.arga.bfaa.submission3.db.MappingHelper
import com.arga.bfaa.submission3.db.QueryHelper
import com.arga.bfaa.submission3.models.FavoriteUser
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var adapter: FavoriteAdapter
    private lateinit var dbHelper: QueryHelper

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        dbHelper = QueryHelper.getInstance(applicationContext)
        dbHelper.open()

        recyclerViewFav.layoutManager = LinearLayoutManager(this)
        recyclerViewFav.setHasFixedSize(true)
        adapter = FavoriteAdapter(this)
        recyclerViewFav.adapter = adapter

        val handleThread = HandlerThread("DataObserver")
        handleThread.start()
        val handler = Handler(handleThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadListFavourite()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadListFavourite()
        } else {
            val list = savedInstanceState.getParcelableArrayList<FavoriteUser>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavoriteUser = list
            }
        }

        if (supportActionBar != null) {
            supportActionBar?.title = getString(R.string.favorite)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavoriteUser)
    }

    private fun loadListFavourite() {
        GlobalScope.launch(Dispatchers.Main) {
            progressbar.visibility = View.VISIBLE
            val deferredFav = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favData = deferredFav.await()
            progressbar.visibility = View.INVISIBLE
            if (favData.size > 0) {
                adapter.listFavoriteUser = favData
            } else {
                adapter.listFavoriteUser = ArrayList()
                Toast.makeText(this@FavoriteActivity, resources.getString(R.string.data_null), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadListFavourite()
    }
}