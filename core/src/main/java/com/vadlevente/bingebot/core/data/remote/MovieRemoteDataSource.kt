package com.vadlevente.bingebot.core.data.remote

import com.vadlevente.bingebot.core.data.api.MovieApi
import com.vadlevente.bingebot.core.model.Item.Movie
import javax.inject.Inject

class MovieRemoteDataSource @Inject constructor(
    movieApi: MovieApi,
) : ItemRemoteDataSource<Movie>(movieApi)