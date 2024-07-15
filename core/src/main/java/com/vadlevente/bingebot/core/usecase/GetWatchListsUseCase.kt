package com.vadlevente.bingebot.core.usecase

import com.vadlevente.bingebot.core.data.repository.MovieRepository
import com.vadlevente.bingebot.core.model.WatchList
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class GetWatchListsUseCaseParams(
    val movieId: Int? = null,
)

class GetWatchListsUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) : BaseUseCase<GetWatchListsUseCaseParams, List<WatchList>>() {

    override fun execute(params: GetWatchListsUseCaseParams): Flow<List<WatchList>> =
        movieRepository.getWatchLists().map {
            it.filter {
                params.movieId?.let { movieId ->
                    !it.movieIds.contains(movieId)
                } ?: true
            }
        }

}