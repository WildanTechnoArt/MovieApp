package com.wildantechnoart.movieapp.network

import com.wildantechnoart.movieapp.model.DetailMovieModel
import com.wildantechnoart.movieapp.model.GenresModel
import com.wildantechnoart.movieapp.model.MovieModel
import com.wildantechnoart.movieapp.model.ReviewModel
import com.wildantechnoart.movieapp.model.TrailerModel
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface BaseApiService {

    @Headers("Accept: application/json")
    @GET("genre/movie/list")
    suspend fun getGenreList(
        @Header("Authorization") token: String?,
    ): GenresModel

    @Headers("Accept: application/json")
    @GET("discover/movie")
    suspend fun getMovieList(
        @Header("Authorization") token: String?,
        @Query("page") page: Int?,
        @Query("with_genres") genres: String?,
    ): MovieModel

    @Headers("Accept: application/json")
    @GET("movie/{movie_id}")
    suspend fun getDetailMovie(
        @Header("Authorization") token: String?,
        @Path("movie_id") id: String?
    ): DetailMovieModel

    @Headers("Accept: application/json")
    @GET("movie/{movie_id}/videos")
    suspend fun getTrailerMovie(
        @Header("Authorization") token: String?,
        @Path("movie_id") id: String?
    ): TrailerModel

    @Headers("Accept: application/json")
    @GET("movie/{movie_id}/reviews")
    suspend fun getReviewMovie(
        @Header("Authorization") token: String?,
        @Path("movie_id") id: String?,
        @Query("page") page: Int?
    ): ReviewModel
}