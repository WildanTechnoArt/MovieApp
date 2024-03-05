package com.wildantechnoart.movieapp.model

import com.google.gson.annotations.SerializedName

data class DetailMovieModel(
    @SerializedName("backdrop_path") var backdropPath: String? = null,
    @SerializedName("genres") var genres: ArrayList<Genres> = arrayListOf(),
    @SerializedName("homepage") var homepage: String? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("overview") var overview: String? = null,
    @SerializedName("poster_path") var posterPath: String? = null,
    @SerializedName("release_date") var releaseDate: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("video") var video: Boolean? = null,
    @SerializedName("vote_average") var voteAverage: Double? = null
)