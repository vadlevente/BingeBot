package com.vadlevente.bingebot.core.model.dto

import com.vadlevente.bingebot.core.model.Movie

data class MoviesResponseDto(
    val results: List<Movie>
)
