package com.vadlevente.bingebot.core.usecase

import com.vadlevente.bingebot.core.data.repository.MovieRepository
import com.vadlevente.bingebot.core.ui.BaseUseCase
import java.util.Date
import javax.inject.Inject

data class SetMovieSeenUseCaseParams(
    val movieId: Int,
    val watchedDate: Date?,
)

class SetMovieSeenUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) : BaseUseCase<SetMovieSeenUseCaseParams, Unit>() {

    override fun execute(params: SetMovieSeenUseCaseParams) = emptyFlow {
        movieRepository.setMovieWatchedDate(params.movieId, params.watchedDate)
    }

}