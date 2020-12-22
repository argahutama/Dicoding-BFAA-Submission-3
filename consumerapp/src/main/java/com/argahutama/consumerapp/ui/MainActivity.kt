package com.argahutama.consumerapp.ui

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.argahutama.consumerapp.R
import com.argahutama.consumerapp.adapter.FavoriteAdapter
import com.argahutama.consumerapp.db.DbContract.UserFavoriteColumns.Companion.CONTENT_URI
import com.argahutama.consumerapp.db.MappingHelper
import com.argahutama.consumerapp.models.FavoriteUser
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerViewFav.layoutManager = LinearLayoutManager(this)
        recyclerViewFav.setHasFixedSize(true)
        adapter = FavoriteAdapter(this)
        recyclerViewFav.adapter = adapter

        val handleThread = HandlerThread("DataObserver")
        handleThread.start()
        val handler = Handler(handleThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadListFavorite()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadListFavorite()
        } else {
            val list = savedInstanceState.getParcelableArrayList<FavoriteUser>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavorite = list
            }
        }

    }

    private fun loadListFavorite() {
        GlobalScope.launch(Dispatchers.Main) {
            progressbar.visibility = View.VISIBLE
            val deferredFav = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favData = deferredFav.await()
            progressbar.visibility = View.INVISIBLE
            if (favData.size > 0) {
                adapter.listFavorite = favData
            } else {
                adapter.listFavorite = ArrayList()
                showSnack()
            }
        }
    }

    private fun showSnack() {
        Snackbar.make(recyclerViewFav, "Data Is Null", Snackbar.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavorite)
    }

    override fun onResume() {
        super.onResume()
        loadListFavorite()
    }

}
