package com.wildantechnoart.movieapp.model

import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("status_message") var message: String? = null
)