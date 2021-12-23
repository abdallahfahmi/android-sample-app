package com.fawry.task.ui.main.movie_details

import android.util.Log
import androidx.lifecycle.*
import com.fawry.task.data.models.Movie
import com.fawry.task.data.network.RemoteResult
import com.fawry.task.data.repositories.movies_repository.IMoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsFragmentViewModel @Inject constructor(
    repository: IMoviesRepository,
    private val savedStateHandle: SavedStateHandle //get values from navArgs
) : ViewModel() {

    private val _movie = MutableLiveData<RemoteResult<Movie>>()
    val movie: LiveData<RemoteResult<Movie>> get() = _movie

    init {
        viewModelScope.launch {
            val result = repository.fetchMovieDetails(savedStateHandle.get<Int>("movieId")?:0)
            _movie.postValue(result)
        }
    }

}