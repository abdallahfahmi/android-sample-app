package com.fawry.task.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.fawry.task.data.models.Category
import com.fawry.task.data.network.RemoteResult
import com.fawry.task.data.repositories.movies_repository.IMoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: IMoviesRepository
) : ViewModel() {

    val cats = liveData<RemoteResult<List<Category>>>(viewModelScope.coroutineContext) {
//        emit(repository.fetchCategories())
    }

    init {
        viewModelScope.launch {
            val categories = repository.fetchCategories()
            categories.data?.forEach {
                fetchMoviesByCategory(it.id)
            }
        }
    }


    fun fetchMoviesByCategory(categoryId: Int) {
        viewModelScope.launch {
            repository.fetchMovies(categoryId)
        }
    }

}