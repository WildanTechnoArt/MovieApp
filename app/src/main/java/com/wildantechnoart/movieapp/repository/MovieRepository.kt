package com.wildantechnoart.movieapp.repository

import com.wildantechnoart.movieapp.network.BaseApiService
import kotlinx.coroutines.flow.flow

class MovieRepository(private val baseApi: BaseApiService) {

    private val token = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1YTExNjAyZDg2NGUwOGVlMDQ2NmU2YzU3MjkzY2MxNyIsInN1YiI6IjVjMGNkNjY3YzNhMzY4MjUyYTBhMzljZCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.v1cFyJ30SwZHgliTrGl9orfHOClZoOMapIdAc0GjczQ"

    fun getGenreList() = flow {
        val response = baseApi.getGenreList("Bearer $token")
        emit(response)
    }

    fun getMovieList(page: Int?, genreId: String?) = flow {
        val response = baseApi.getMovieList("Bearer $token", page, genreId)
        emit(response)
    }

    fun getDetailMovie(id: String?) = flow {
        val response = baseApi.getDetailMovie("Bearer $token", id)
        emit(response)
    }

    fun getTrailerMovie(id: String?) = flow {
        val response = baseApi.getTrailerMovie("Bearer $token", id)
        emit(response)
    }

    fun getReviewMovie(page: Int?, id: String?) = flow {
        val response = baseApi.getReviewMovie("Bearer $token", id, page)
        emit(response)
    }
}