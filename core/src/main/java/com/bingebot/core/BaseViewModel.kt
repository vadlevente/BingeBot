package com.bingebot.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bingebot.core.events.dialog.DialogEvent.ShowDialog
import com.bingebot.core.events.dialog.DialogEventChannel
import com.bingebot.core.events.dialog.DialogResponse
import com.bingebot.core.events.dialog.DialogResponse.NEGATIVE
import com.bingebot.core.events.dialog.DialogResponse.POSITIVE
import com.bingebot.core.events.navigation.NavigationEventChannel
import com.bingebot.core.model.exception.BingeBotException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
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
                ?: stringOf(R.string.errorMessage_unknown)
        }
        viewModelScope.launch {
            dialogEventChannel.sendEvent(
                ShowDialog(
                    title = stringOf(R.string.errorTitle),
                    content = errorMessage,
                    positiveButtonTitle = stringOf(R.string.common_Ok),
                )
            )
        }
    }

    protected suspend fun <T : Any> Flow<T>.collectSimple(
        collector: FlowCollector<T> = FlowCollector {},
        errorHandler: (Throwable) -> Unit = basicErrorHandler,
    ) =
        this
            .onStart { isInProgressMutable.update { true } }
            .onCompletion { isInProgressMutable.update { false } }
            .catch {
                errorHandler(it)
            }
            .collect(collector)

    protected suspend fun Flow<Any>.collectSilent(
        collector: FlowCollector<Any> = FlowCollector {},
        errorHandler: (Throwable) -> Unit = basicErrorHandler,
    ) = this
        .catch {
            errorHandler(it)
        }
        .collect(collector)

    protected suspend fun showDialog(event: ShowDialog) = MutableSharedFlow<DialogResponse>()
        .apply {
            dialogEventChannel.sendEvent(
                event.copy(
                    onPositiveButtonClicked = { tryEmit(POSITIVE) },
                    onNegativeButtonClicked = { tryEmit(NEGATIVE) },
                )
            )
        }.asSharedFlow()


}