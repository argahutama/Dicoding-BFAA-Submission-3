package com.argahutama.consumerapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GithubUser(
    var username: String = "",
    var name: String? = "",
    var avatar: String? = "",
    var url: String? = "",
    var location: String? = "",
    var repositoryName: String? = "",
    var repositoryUrl: String? = "",
    var repositoryLanguage: String? = "",
    var repository: Int = 0,
    var followers: Int = 0,
    var following: Int = 0
) : Parcelable