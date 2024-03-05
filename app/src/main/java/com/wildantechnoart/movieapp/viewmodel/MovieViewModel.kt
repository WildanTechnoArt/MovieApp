package com.wildantechnoart.movieapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wildantechnoart.movieapp.model.DetailMovieModel
import com.wildantechnoart.movieapp.model.Genres
import com.wildantechnoart.movieapp.model.MovieModel
import com.wildantechnoart.movieapp.model.ResultsTrailer
import com.wildantechnoart.movieapp.model.ReviewModel
import com.wildantechnoart.movieapp.model.TrailerModel
import com.wildantechnoart.movieapp.network.RetrofitClient
import com.wildantechnoart.movieapp.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {

    private val repository: MovieRepository by lazy { MovieRepository(RetrofitClient.instance) }

    private val _getPeopleList = MutableLiveData<List<Genres>>()
    val getGenreList: LiveData<List<Genres>> = _getPeopleList

    private val _getMovieList = MutableLiveData<MovieModel?>()
    val getMovieList: LiveData<MovieModel?> = _getMovieList

    private val _getReviewList = MutableLiveData<ReviewModel?>()
    val getReviewList: LiveData<ReviewModel?> = _getReviewList

    private val _getDetailMovie = MutableLiveData<DetailMovieModel?>()
    val getDetailMovie: LiveData<DetailMovieModel?> = _getDetailMovie

    private val _getTrailerMovie = MutableLiveData<List<ResultsTrailer>>()
    val getTrailerMovie: LiveData<List<ResultsTrailer>> = _getTrailerMovie

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> = _error

    fun getGenreList() {
        viewModelScope.launch {
            try {
                repository.getGenreList()
                    .flowOn(Dispatchers.IO)
                    .onStart { _loading.value = true }
                    .onCompletion { _loading.postValue(false) }
                    .catch { errorHandle(it) }
                    .collect {
                        _getPeopleList.value = it.genres
                    }
            } catch (e: Exception) {
                errorHandle(e)
            }
        }
    }

    fun getMovieList(page: Int?, genreId: String?) {
        viewModelScope.launch {
            try {
                repository.getMovieList(page, genreId)
                    .flowOn(Dispatchers.IO)
                    .onStart { _loading.value = true }
                    .onCompletion { _loading.postValue(false) }
                    .catch { errorHandle(it) }
                    .collect {
                        _getMovieList.value = it
                    }
            } catch (e: Exception) {
                errorHandle(e)
            }
        }
    }

    fun getReviewMovie(page: Int?, id: String?) {
        viewModelScope.launch {
            try {
                repository.getReviewMovie(page, id)
                    .flowOn(Dispatchers.IO)
                    .onStart { _loading.value = true }
                    .onCompletion { _loading.postValue(false) }
                    .catch { errorHandle(it) }
                    .collect {
                        _getReviewList.value = it
                    }
            } catch (e: Exception) {
                errorHandle(e)
            }
        }
    }

    fun getDetailMovie(id: String?) {
        viewModelScope.launch {
            try {
                repository.getDetailMovie(id)
                    .flowOn(Dispatchers.IO)
                    .onStart { _loading.value = true }
                    .onCompletion { _loading.postValue(false) }
                    .catch { errorHandle(it) }
                    .collect {
                        _getDetailMovie.value = it
                    }
            } catch (e: Exception) {
                errorHandle(e)
            }
        }
    }

    fun getTrailerMovie(id: String?) {
        viewModelScope.launch {
            try {
                repository.getTrailerMovie(id)
                    .flowOn(Dispatchers.IO)
                    .onStart { _loading.value = true }
                    .onCompletion { _loading.postValue(false) }
                    .catch { errorHandle(it) }
                    .collect {
                        _getTrailerMovie.value = it.results
                    }
            } catch (e: Exception) {
                errorHandle(e)
            }
        }
    }

    private fun errorHandle(it: Throwable) {
        _loading.postValue(false)
        _error.postValue(it)
    }
}