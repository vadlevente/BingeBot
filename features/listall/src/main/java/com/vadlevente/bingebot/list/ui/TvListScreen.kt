package com.vadlevente.bingebot.list.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadlevente.bingebot.list.R
import com.vadlevente.bingebot.list.TvListViewModel
import com.vadlevente.bingebot.resources.R as Res

@Composable
fun TvListScreen(
    viewModel: TvListViewModel = hiltViewModel(),
) {
    ItemListScreen(
        viewModel = viewModel,
        resources = ItemListScreenResources(
            title = R.string.tvList_pageTitle,
            searchFieldHint = Res.string.searchField_tvHint,
            emptyListDescription = Res.string.emptyList_tv_Description,
        )
    )
}