package com.fawry.task.data.di

import com.fawry.task.data.database.AppDatabase
import com.fawry.task.data.network.APIsService
import com.fawry.task.data.repositories.movies_repository.IMoviesRepository
import com.fawry.task.data.repositories.movies_repository.MoviesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class RepositoriesModule {

    @Provides
    @ViewModelScoped
    fun moviesRepository(service: APIsService, database: AppDatabase): IMoviesRepository =
        MoviesRepository(service, database)

}