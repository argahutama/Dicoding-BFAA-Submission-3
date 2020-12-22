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

class MainViewModel : ViewModel() {
    private val listGithubUsers = MutableLiveData<ArrayList<GithubUser>>()
    val errorMessage = MutableLiveData<Event<String>>()

    fun setSearchGithubUsers(username: String) {
        val listUsers = ArrayList<GithubUser>()
        val client = AsyncHttpClient()
        client.addHeader("Authorization", BuildConfig.GITHUB_API_KEY)
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/search/users?q=$username"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int,
                                   headers: Array<Header>?,
                                   responseBody: ByteArray) {
                val result = String(responseBody)
                try {
                    val responseObject = JSONObject(result)
                    val jsonArray = responseObject.getJSONArray("items")
                    if (jsonArray.length() == 0){
                        errorMessage.value = Event("User not found!")
                    }
                    for (i in 0 until jsonArray.length()) {
                        val user = jsonArray.getJSONObject(i)
                        val githubUser = GithubUser()
                        githubUser.username = user.getString("login")
                        githubUser.avatar = user.getString("avatar_url")
                        githubUser.url = user.getString("html_url")
                        listUsers.add(githubUser)
                    }
                    listGithubUsers.postValue(listUsers)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int,
                                   headers: Array<Header>?,
                                   responseBody: ByteArray?,
                                   error: Throwable) {
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

    fun getSearchGithubUsers(): LiveData<ArrayList<GithubUser>> {
        return listGithubUsers
    }
}