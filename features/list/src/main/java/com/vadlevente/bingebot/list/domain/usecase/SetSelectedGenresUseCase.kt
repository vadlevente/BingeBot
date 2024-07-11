package com.vadlevente.bingebot.list.domain.usecase

import com.vadlevente.bingebot.core.data.cache.SelectedGenresCacheDataSource
import com.vadlevente.bingebot.core.model.Genre
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class SetSelectedGenresUseCaseParams(
    val genres: List<Genre>,
)
class SetSelectedGenresUseCase @Inject constructor(
    private val selectedGenresCacheDataSource: SelectedGenresCacheDataSource,
) : BaseUseCase<SetSelectedGenresUseCaseParams, Unit>() {

    override fun execute(params: SetSelectedGenresUseCaseParams): Flow<Unit> = emptyFlow {
        selectedGenresCacheDataSource.updateGenres(params.genres)
    }

}