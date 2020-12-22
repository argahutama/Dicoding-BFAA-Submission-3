package com.arga.bfaa.submission3.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arga.bfaa.submission3.BuildConfig
import com.arga.bfaa.submission3.models.GithubUser
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailViewModel : ViewModel() {
    private val detailGithubUser = MutableLiveData<GithubUser>()

    fun setDetailGithubUser(username: String) {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", BuildConfig.GITHUB_API_KEY)
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$username"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>?,
                                   responseBody: ByteArray) {
                val result = String(responseBody)
                try {
                    val responseObject = JSONObject(result)
                    val githubUser = GithubUser()
                    githubUser.avatar = responseObject.getString("avatar_url")
                    githubUser.name = responseObject.getString("name")
                    githubUser.username = responseObject.getString("login")
                    githubUser.repository = responseObject.getInt("public_repos")
                    githubUser.location = responseObject.getString("location")
                    githubUser.followers = responseObject.getInt("followers")
                    githubUser.following = responseObject.getInt("following")
                    detailGithubUser.postValue(githubUser)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int,
                                   headers: Array<Header>?,
                                   responseBody: ByteArray?,
                                   error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    fun getDetailGithubUsers(): LiveData<GithubUser> {
        return detailGithubUser
    }
}