package com.wildantechnoart.movieapp.model

import com.google.gson.annotations.SerializedName

data class TrailerModel(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("results") var results: ArrayList<ResultsTrailer> = arrayListOf()
)

data class ResultsTrailer(
    @SerializedName("name") var name: String? = null,
    @SerializedName("key") var key: String? = null,
    @SerializedName("type") var type: String? = null
)