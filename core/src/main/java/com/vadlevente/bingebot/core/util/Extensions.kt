package com.vadlevente.bingebot.core.util

import com.vadlevente.bingebot.core.model.ApiConfiguration
import com.vadlevente.bingebot.core.model.CastMember
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.model.PersonCredit
import com.vadlevente.bingebot.core.model.PersonDetails

fun Item.getThumbnailUrl(configuration: ApiConfiguration) =
    posterPath?.let {
        "${configuration.imageConfiguration.thumbnailBaseUrl}${it}"
    }

fun PersonCredit.getPosterUrl(configuration: ApiConfiguration) =
    posterPath?.let {
        "${configuration.imageConfiguration.thumbnailBaseUrl}${it}"
    }

fun CastMember.getProfileUrl(configuration: ApiConfiguration) =
    profileUrl?.let {
        "${configuration.imageConfiguration.thumbnailBaseUrl}${profileUrl}"
    }

fun PersonDetails.getProfileUrl(configuration: ApiConfiguration) =
    profileUrl?.let {
        "${configuration.imageConfiguration.thumbnailBaseUrl}${profileUrl}"
    }
