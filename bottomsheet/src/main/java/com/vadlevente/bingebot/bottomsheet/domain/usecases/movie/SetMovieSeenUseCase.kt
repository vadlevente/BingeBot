package com.vadlevente.bingebot.bottomsheet.domain.usecases.movie

import com.vadlevente.bingebot.bottomsheet.domain.usecases.SetItemSeenUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.SetItemSeenUseCaseParams
import com.vadlevente.bingebot.core.data.repository.MovieRepository
import javax.inject.Inject
class SetMovieSeenUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) : SetItemSeenUseCase {

    override fun execute(params: SetItemSeenUseCaseParams) = emptyFlow {
        movieRepository.setMovieWatchedDate(params.itemId, params.watchedDate)
    }

}