package me.sambains.androidapisample.core.models

import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("id")
    var id: Long,

    @SerializedName("name")
    var name: String
)
