package com.vadlevente.bingebot.core.data.remote

import com.vadlevente.bingebot.core.data.api.MovieApi
import com.vadlevente.bingebot.core.model.SelectedLanguage
import javax.inject.Inject

class MovieRemoteDataSource @Inject constructor(
    private val movieApi: MovieApi,
) {

    suspend fun getConfiguration() = movieApi.fetchConfiguration()
    suspend fun getGenres(language: SelectedLanguage) = movieApi.fetchGenres(language.code)
    suspend fun searchMovie(
        query: String,
        language: SelectedLanguage,
    ) = movieApi.searchMovies(query, language.code).results
    suspend fun getMovieDetails(
        movieId: Int,
        language: SelectedLanguage,
    ) = movieApi.fetchMovieDetails(movieId, language.code)

}