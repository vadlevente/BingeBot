package com.vadlevente.bingebot.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.core.R.string
import com.vadlevente.bingebot.core.UIText
import com.vadlevente.bingebot.core.events.navigation.NavigationEvent.NavigateTo
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEvent.ShowToast
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastType
import com.vadlevente.bingebot.core.events.toast.ToastType.ERROR
import com.vadlevente.bingebot.core.model.NavDestination
import com.vadlevente.bingebot.core.model.exception.BingeBotException
import com.vadlevente.bingebot.core.model.exception.Reason.SESSION_EXPIRED
import com.vadlevente.bingebot.core.model.exception.isBecauseOf
import com.vadlevente.bingebot.core.stringOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface State

object EmptyState : State

abstract class BaseViewModel<S : State>(
    private val navigationEventChannel: NavigationEventChannel,
    protected val toastEventChannel: ToastEventChannel,
) : ViewModel() {

    abstract val state: StateFlow<S>

    private val isInProgressMutable = MutableStateFlow(false)
    protected val isInProgress = isInProgressMutable

    open val basicErrorHandler: (Throwable) -> Unit = { t ->
        val errorMessage = when (t) {
            is BingeBotException -> t.reason?.reasonText ?: t.errorMessage ?: stringOf(string.errorMessage_unknown)
            else -> t.localizedMessage?.let { stringOf(it) }
                ?: stringOf(string.errorMessage_unknown)
        }
        viewModelScope.launch {
            toastEventChannel.sendEvent(
                ShowToast(
                    message = errorMessage,
                    type = ERROR,
                )
            )
        }
        if (t.isBecauseOf(SESSION_EXPIRED)) {
            navigateTo(NavDestination.SPLASH)
        }
    }

    protected fun <T : Any> Flow<T>.onValue(
        action: suspend (T) -> Unit,
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

    protected fun navigateTo(navDestination: NavDestination) {
        viewModelScope.launch {
            navigationEventChannel.sendEvent(NavigateTo(navDestination.route))
        }
    }

    protected fun showToast(message: UIText, type: ToastType) {
        viewModelScope.launch {
            toastEventChannel.sendEvent(ShowToast(message, type))
        }
    }


}