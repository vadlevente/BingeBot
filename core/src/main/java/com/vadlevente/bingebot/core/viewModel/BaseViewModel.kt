package com.vadlevente.bingebot.core.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.core.UIText
import com.vadlevente.bingebot.core.events.navigation.NavigationEvent.NavigateTo
import com.vadlevente.bingebot.core.events.navigation.NavigationEvent.NavigateUp
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
import com.vadlevente.bingebot.resources.R.string
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
import timber.log.Timber
import com.vadlevente.bingebot.resources.R as Res

interface State

object EmptyState : State

abstract class BaseViewModel<S : State>(
    private val navigationEventChannel: NavigationEventChannel,
    protected val toastEventChannel: ToastEventChannel,
) : ViewModel() {

    abstract val state: StateFlow<S>

    private val isInProgressMutable = MutableStateFlow(false)
    val isInProgress = isInProgressMutable

    open val basicErrorHandler: (Throwable) -> Unit = { t ->
        Timber.e(t)
        val errorMessage = when (t) {
            is BingeBotException -> t.reason?.reasonText ?: t.errorMessage ?: stringOf(Res.string.errorMessage_unknown)
            else -> t.localizedMessage?.let { stringOf(it) }
                ?: stringOf(Res.string.errorMessage_unknown)
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
            .onEmpty { isInProgressMutable.update { false } }
            .catch {
                basicErrorHandler(it)
            }
            .onEach { isInProgressMutable.update { false } }
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

    protected fun <T : Any> Flow<T>.onStart() = this
        .onStart { isInProgressMutable.update { true } }
        .onCompletion { isInProgressMutable.update { false } }
        .onEmpty { isInProgressMutable.update { false } }
        .onEach { isInProgressMutable.update { false } }
        .catch {
            basicErrorHandler(it)
        }
        .launchIn(viewModelScope)

    protected fun <T : Any> Flow<T>.onStartSilent() = this
        .catch {
            basicErrorHandler(it)
        }
        .launchIn(viewModelScope)

    protected fun navigateTo(navDestination: NavDestination, vararg arguments: Any) {
        viewModelScope.launch {
            navigationEventChannel.sendEvent(NavigateTo(navDestination.route, arguments.toList()))
        }
    }

    protected fun navigateUp() {
        viewModelScope.launch {
            navigationEventChannel.sendEvent(NavigateUp)
        }
    }

    protected fun showToast(message: UIText, type: ToastType) {
        viewModelScope.launch {
            toastEventChannel.sendEvent(ShowToast(message, type))
        }
    }


}