package com.vadlevente.bingebot.bottomsheet.viewmodel

import com.vadlevente.bingebot.bottomsheet.domain.model.DisplayedOrderBy
import com.vadlevente.bingebot.bottomsheet.domain.usecases.GetOrderByListUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.SetOrderByFilterUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.SetOrderByFilterUseCaseParams
import com.vadlevente.bingebot.bottomsheet.viewmodel.OrderByBottomSheetViewModel.ViewState
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.model.OrderBy
import com.vadlevente.bingebot.core.viewModel.BaseViewModel
import com.vadlevente.bingebot.core.viewModel.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

abstract class OrderByBottomSheetViewModel <T : Item>(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    getOrderByListUseCase: GetOrderByListUseCase<T>,
    private val setOrderByFilterUseCase: SetOrderByFilterUseCase<T>,
) : BaseViewModel<ViewState>(
    navigationEventChannel, toastEventChannel
) {

    protected val viewState = MutableStateFlow(ViewState())
    override val state: StateFlow<ViewState> = viewState

    init {
        getOrderByListUseCase.execute(Unit).onValue { orderByList ->
            viewState.update {
                it.copy(
                    orderByList = orderByList
                )
            }
        }
    }

    fun onChangeOrderBy(value: OrderBy) {
        setOrderByFilterUseCase.execute(
            SetOrderByFilterUseCaseParams(
                orderBy = value
            )
        ).onValue {
            onDismiss()
        }
    }

    fun onDismiss() {
        viewState.update {
            it.copy(
                isVisible = false,
            )
        }
    }

    data class ViewState(
        val isVisible: Boolean = false,
        val orderByList: List<DisplayedOrderBy> = emptyList(),
    ) : State

}