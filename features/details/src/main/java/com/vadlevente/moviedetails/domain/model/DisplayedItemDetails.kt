package com.vadlevente.moviedetails.domain.model

import com.vadlevente.bingebot.core.model.Credits
import com.vadlevente.bingebot.core.model.DisplayedItem
import com.vadlevente.bingebot.core.model.Item

data class DisplayedItemDetails<T : Item>(
    val displayedItem: DisplayedItem<T>,
    val credits: Credits,
)
