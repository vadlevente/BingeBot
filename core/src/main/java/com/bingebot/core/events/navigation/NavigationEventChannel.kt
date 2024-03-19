package com.bingebot.core.events.navigation

import com.bingebot.core.events.EventChannel
import com.bingebot.core.events.EventChannelBase
import javax.inject.Inject

interface NavigationEventChannel : EventChannel<NavigationEvent>

class NavigationEventChannelImpl @Inject constructor(

) : EventChannelBase<NavigationEvent>(), NavigationEventChannel