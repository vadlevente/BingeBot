package com.vadlevente.bingebot.core.events.navigation

import com.vadlevente.bingebot.core.events.EventChannel
import com.vadlevente.bingebot.core.events.EventChannelBase
import javax.inject.Inject

interface NavigationEventChannel : EventChannel<NavigationEvent>

class NavigationEventChannelImpl @Inject constructor(

) : EventChannelBase<NavigationEvent>(), NavigationEventChannel