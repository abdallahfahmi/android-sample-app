package com.fawry.task.data.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.fawry.task.data.models.Category
import com.fawry.task.data.models.Movie
import com.fawry.task.data.network.RemoteResult
import kotlinx.coroutines.*

abstract class NetworkBoundResource<ResultType>(scope: CoroutineScope) {

    val result: MutableLiveData<RemoteResult<ResultType>> = MutableLiveData()

    init {

        //pass viewModelScope to bound all the calls to the lifecycle of the viewModel
        scope.launch(Dispatchers.IO) {

            val data = loadFromDb()

            if (shouldFetch(data)) { //database is empty, fetch from remote

                /**nulllllllllllllllllllllllllllllllllls*/

                result.postValue(RemoteResult.loading())

                val categories = fetchRemoteCategories()

                saveCategoriesToDB(categories.data!!)

                //fetch movies of each category concurrently and save them to database
                val jobs = arrayListOf<Deferred<Unit>>()
                categories.data.forEach {
                    jobs.add(async { fetchRemoteMoviesWriteToDatabase(it.id) })
                }

                //wait for all movies to be fetched and written in database before return the list to the user
                awaitAll(*jobs.toTypedArray())

                result.postValue(RemoteResult.success(loadFromDb()!!))

            } else { //return cached data
                result.postValue(RemoteResult.success(data!!))
            }

        }
    }

    private suspend fun fetchRemoteMoviesWriteToDatabase(categoryId: Int) {
        val movies = fetchRemoteMovies(categoryId)
        saveMoviesToDB(movies.data!!)
        /*********************************************/
    }

    @WorkerThread
    abstract suspend fun loadFromDb(): ResultType?

    @WorkerThread
    abstract fun shouldFetch(data: ResultType?): Boolean

    @WorkerThread
    abstract suspend fun fetchRemoteCategories(): RemoteResult<List<Category>>

    @WorkerThread
    abstract suspend fun fetchRemoteMovies(categoryId: Int): RemoteResult<List<Movie>>

    @WorkerThread
    abstract suspend fun saveCategoriesToDB(data: List<Category>)

    @WorkerThread
    abstract suspend fun saveMoviesToDB(data: List<Movie>)

}