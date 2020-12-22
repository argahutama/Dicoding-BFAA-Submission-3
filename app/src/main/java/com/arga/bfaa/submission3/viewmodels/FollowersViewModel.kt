package com.arga.bfaa.submission3.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arga.bfaa.submission3.BuildConfig
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.arga.bfaa.submission3.models.GithubUser
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowersViewModel : ViewModel() {
    val followersGithubUser = MutableLiveData<ArrayList<GithubUser>>()
    val errorMessage = MutableLiveData<Event<String>>()

    fun setFollowersGithubUser(username: String) {
        val listFollowers = ArrayList<GithubUser>()
        val client = AsyncHttpClient()
        client.addHeader("Authorization", BuildConfig.GITHUB_API_KEY)
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$username/followers"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int,
                                   headers: Array<Header>?,
                                   responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseArray = JSONArray(result)
                    for(i in 0 until responseArray.length()){
                        val jsonObject = responseArray.getJSONObject(i)
                        val githubUser = GithubUser()
                        githubUser.username = jsonObject.getString("login")
                        githubUser.avatar = jsonObject.getString("avatar_url")
                        githubUser.url = jsonObject.getString("html_url")
                        listFollowers.add(githubUser)
                    }
                    followersGithubUser.postValue(listFollowers)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, responseBody: ByteArray?, error: Throwable) {
                Log.d("onFailure", error.message.toString())

                val message = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : Check Your Connectivity"
                }
                errorMessage.value = Event(message)
            }
        })
    }

    fun getFollowersGithubUsers(): LiveData<ArrayList<GithubUser>> {
        return followersGithubUser
    }
}