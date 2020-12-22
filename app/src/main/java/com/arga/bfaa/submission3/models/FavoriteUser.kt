package com.arga.bfaa.submission3.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FavoriteUser(
    var username: String = "",
    var avatar: String? = "",
    var url: String? = "",
    var isFav: String? = ""
) : Parcelable