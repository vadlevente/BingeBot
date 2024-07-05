package com.vadlevente.bingebot.splash.usecase

import com.vadlevente.bingebot.core.data.repository.MovieRepository
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetConfigurationUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) : BaseUseCase<Unit, Unit>() {

    override fun execute(params: Unit): Flow<Unit> = emptyFlow {
        movieRepository.updateConfiguration()
    }

}