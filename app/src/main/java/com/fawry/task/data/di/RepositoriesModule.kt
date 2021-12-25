package com.fawry.task.data.di

import com.fawry.task.data.database.AppDatabase
import com.fawry.task.data.network.APIsService
import com.fawry.task.data.repositories.movies_repository.IMoviesRepository
import com.fawry.task.data.repositories.movies_repository.MoviesRepository
import com.fawry.task.data.repositories.movies_repository.data_source.LocalDataSource
import com.fawry.task.data.repositories.movies_repository.data_source.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
//@InstallIn(ViewModelComponent::class)
@InstallIn(SingletonComponent::class)
class RepositoriesModule {

    @Provides
    @Singleton
    fun provideMoviesLocalDataSource(database: AppDatabase) = LocalDataSource(database)

    @Provides
    @Singleton
    fun provideMoviesRemoteDataSource(apisService: APIsService) = RemoteDataSource(apisService)

    @Provides
    @Singleton
//    @ViewModelScoped
    fun moviesRepository(remote: RemoteDataSource, local: LocalDataSource): IMoviesRepository =
        MoviesRepository(remote, local)

}