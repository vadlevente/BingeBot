package com.vadlevente.bingebot.core.data.remote

import com.vadlevente.bingebot.core.data.api.TvApi
import com.vadlevente.bingebot.core.model.Item.Tv
import javax.inject.Inject

class TvRemoteDataSource @Inject constructor(
    tvApi: TvApi,
) : ItemRemoteDataSource<Tv>(tvApi)