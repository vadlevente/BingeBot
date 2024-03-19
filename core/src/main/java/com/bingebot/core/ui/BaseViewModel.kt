package com.bingebot.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bingebot.core.R.string
import com.bingebot.core.events.dialog.DialogEvent.ShowDialog
import com.bingebot.core.events.dialog.DialogEventChannel
import com.bingebot.core.events.dialog.DialogResponse
import com.bingebot.core.events.dialog.DialogResponse.NEGATIVE
import com.bingebot.core.events.dialog.DialogResponse.POSITIVE
import com.bingebot.core.events.navigation.NavigationEvent.NavigateTo
import com.bingebot.core.events.navigation.NavigationEventChannel
import com.bingebot.core.model.NavDestination
import com.bingebot.core.model.exception.BingeBotException
import com.bingebot.core.stringOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

interface State

object EmptyState : State

abstract class BaseViewModel<S : State>(
) : ViewModel() {

    abstract val state: StateFlow<S>

    private val isInProgressMutable = MutableStateFlow(false)
    protected val isInProgress = isInProgressMutable

    @Inject
    protected lateinit var navigationEventChannel: NavigationEventChannel

    @Inject
    protected lateinit var dialogEventChannel: DialogEventChannel

    open val basicErrorHandler: (Throwable) -> Unit = { t ->
        val errorMessage = when (t) {
            is BingeBotException -> t.reason.reasonText
            else -> t.localizedMessage?.let { stringOf(it) }
                ?: stringOf(string.errorMessage_unknown)
        }
        viewModelScope.launch {
            dialogEventChannel.sendEvent(
                ShowDialog(
                    title = stringOf(string.errorTitle),
                    content = errorMessage,
                    positiveButtonTitle = stringOf(string.common_Ok),
                )
            )
        }
    }

    protected fun <T : Any> Flow<T>.onValue(
        action: (T) -> Unit,
    ) =
        this
            .onStart { isInProgressMutable.update { true } }
            .onCompletion { isInProgressMutable.update { false } }
            .onEmpty {
                var a = 1
            }
            .catch {
                basicErrorHandler(it)
            }
            .onEach(action)
            .launchIn(viewModelScope)

    protected fun <T : Any> Flow<T>.onValueSilent(
        action: (T) -> Unit,
    ) = this
        .catch {
            basicErrorHandler(it)
        }
        .onEach(action)
        .launchIn(viewModelScope)

    protected suspend fun showDialog(event: ShowDialog) = MutableSharedFlow<DialogResponse>()
        .apply {
            dialogEventChannel.sendEvent(
                event.copy(
                    onPositiveButtonClicked = { tryEmit(POSITIVE) },
                    onNegativeButtonClicked = { tryEmit(NEGATIVE) },
                )
            )
        }.asSharedFlow()

    protected fun navigateTo(navDestination: NavDestination) {
        viewModelScope.launch {
            navigationEventChannel.sendEvent(NavigateTo(navDestination.route))
        }
    }


}