package com.vadlevente.bingebot.settings.domain.usecases

import com.vadlevente.bingebot.core.data.cache.SelectedFiltersCacheDataSource
import com.vadlevente.bingebot.core.data.repository.ConfigurationRepository
import com.vadlevente.bingebot.core.model.Item.Movie
import com.vadlevente.bingebot.core.model.Item.Tv
import com.vadlevente.bingebot.core.usecase.BaseUseCase
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val configurationRepository: ConfigurationRepository,
    private val movieSelectedFiltersCacheDataSource: SelectedFiltersCacheDataSource<Movie>,
    private val tvSelectedFiltersCacheDataSource: SelectedFiltersCacheDataSource<Tv>
) : BaseUseCase<Unit, Unit> {

    override fun execute(params: Unit) = emptyFlow {
        movieSelectedFiltersCacheDataSource.clear()
        tvSelectedFiltersCacheDataSource.clear()
        configurationRepository.logout()
    }

}