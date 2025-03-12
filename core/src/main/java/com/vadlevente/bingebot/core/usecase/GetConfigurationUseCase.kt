package com.vadlevente.bingebot.core.usecase

import com.vadlevente.bingebot.core.data.repository.ConfigurationRepository
import com.vadlevente.bingebot.core.data.repository.ItemRepository
import com.vadlevente.bingebot.core.model.Item.Movie
import com.vadlevente.bingebot.core.model.Item.Tv
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetConfigurationUseCase @Inject constructor(
    private val configurationRepository: ConfigurationRepository,
    private val movieRepository: ItemRepository<Movie>,
    private val tvRepository: ItemRepository<Tv>,
) : BaseUseCase<Unit, Unit> {

    override fun execute(params: Unit): Flow<Unit> = emptyFlow {
        configurationRepository.updateConfiguration()
        movieRepository.updateGenres()
        tvRepository.updateGenres()
    }

}