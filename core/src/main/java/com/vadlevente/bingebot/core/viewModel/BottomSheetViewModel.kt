package com.vadlevente.bingebot.core.viewModel

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.core.R
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEvent.ShowMovieBottomSheet
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEventChannel
import com.vadlevente.bingebot.core.events.dialog.DialogEvent.ShowDialog
import com.vadlevente.bingebot.core.events.dialog.DialogEventChannel
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastType.INFO
import com.vadlevente.bingebot.core.stringOf
import com.vadlevente.bingebot.core.usecase.DeleteMovieUseCase
import com.vadlevente.bingebot.core.usecase.DeleteMovieUseCaseParams
import com.vadlevente.bingebot.core.viewModel.BottomSheetViewModel.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomSheetViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    bottomSheetEventChannel: BottomSheetEventChannel,
    private val dialogEventChannel: DialogEventChannel,
    private val deleteMovieUseCase: DeleteMovieUseCase,
) : BaseViewModel<ViewState>(
    navigationEventChannel, toastEventChannel
) {

    private val viewState = MutableStateFlow(ViewState())
    override val state: StateFlow<ViewState> = viewState

    init {
        bottomSheetEventChannel.events.filterIsInstance<ShowMovieBottomSheet>().onEach { event ->
            viewState.update {
                it.copy(
                    isVisible = true,
                    event = event,
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onDismiss() {
        viewState.update {
            it.copy(
                isVisible = false,
            )
        }
    }

    fun onAddToWatchList() {

    }

    fun onDelete() {
        val movieId = viewState.value.event?.movieId ?: return
        viewModelScope.launch {
            dialogEventChannel.sendEvent(
                ShowDialog(
                    title = stringOf(R.string.movieBottomSheet_deleteConfirmationTitle),
                    content = stringOf(R.string.movieBottomSheet_deleteConfirmationDescription),
                    positiveButtonTitle = stringOf(R.string.common_Yes),
                    negativeButtonTitle = stringOf(R.string.common_No),
                    onPositiveButtonClicked = {
                        deleteMovieUseCase.execute(
                            DeleteMovieUseCaseParams(movieId)
                        )
                            .onValue {
                                onDismiss()
                                showToast(
                                    message = stringOf(R.string.movieBottomSheet_movieDeletedToast),
                                    type = INFO,
                                )
                            }
                    },
                )
            )
        }
    }

    fun onShowDetails() {

    }

    data class ViewState(
        val isVisible: Boolean = false,
        val event: ShowMovieBottomSheet? = null,
    ) : State

}