package com.vadlevente.bingebot.list.usecase

import com.vadlevente.bingebot.core.data.repository.MovieRepository
import com.vadlevente.bingebot.core.model.Genre
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGenresUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) : BaseUseCase<Unit, List<Genre>>() {

    override fun execute(params: Unit): Flow<List<Genre>> = movieRepository.getGenres()

}