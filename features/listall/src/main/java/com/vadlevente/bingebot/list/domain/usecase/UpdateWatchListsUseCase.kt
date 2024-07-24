package com.vadlevente.bingebot.list.domain.usecase

import com.vadlevente.bingebot.core.data.repository.MovieRepository
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateWatchListsUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) : BaseUseCase<Unit, Unit>() {

    override fun execute(params: Unit): Flow<Unit> = emptyFlow {
        movieRepository.updateWatchLists()
    }

}