package com.fawry.task.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fawry.task.data.models.CategorizedMovies
import com.fawry.task.data.network.RemoteResult
import com.fawry.task.data.repositories.movies_repository.IMoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: IMoviesRepository
) : ViewModel() {

    val movies by lazy {
        repository.getMoviesCategorized(viewModelScope)
    }

}