package com.vadlevente.bingebot.bottomsheet.domain.usecases.movie

import com.vadlevente.bingebot.bottomsheet.domain.usecases.GetWatchListsUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.GetWatchListsUseCaseParams
import com.vadlevente.bingebot.core.data.repository.MovieRepository
import com.vadlevente.bingebot.core.model.WatchList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMovieWatchListsUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) : GetWatchListsUseCase {

    override fun execute(params: GetWatchListsUseCaseParams): Flow<List<WatchList>> =
        movieRepository.getWatchLists().map {
            it.filter {
                params.itemId?.let { movieId ->
                    !it.movieIds.contains(movieId)
                } ?: true
            }
        }

}