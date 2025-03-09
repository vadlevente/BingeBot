package com.vadlevente.bingebot.core.util

import com.vadlevente.bingebot.core.model.ApiConfiguration
import com.vadlevente.bingebot.core.model.CastMember
import com.vadlevente.bingebot.core.model.Item

fun Item.getThumbnailUrl(configuration: ApiConfiguration) =
    posterPath?.let {
        "${configuration.imageConfiguration.thumbnailBaseUrl}${it}"
    }

fun CastMember.getProfileUrl(configuration: ApiConfiguration) =
        "${configuration.imageConfiguration.thumbnailBaseUrl}${profileUrl}"
