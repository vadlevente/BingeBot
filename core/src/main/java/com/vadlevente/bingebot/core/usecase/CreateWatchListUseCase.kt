package com.vadlevente.bingebot.core.usecase

import com.vadlevente.bingebot.core.data.repository.MovieRepository
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

data class CreateWatchListUseCaseParams(
    val title: String,
)

class CreateWatchListUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) : BaseUseCase<CreateWatchListUseCaseParams, String>() {

    override fun execute(params: CreateWatchListUseCaseParams): Flow<String> = flow {
        val watchListId = movieRepository.createWatchList(params.title)
        emit(watchListId)
    }

}