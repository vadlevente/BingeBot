package com.bingebot.core.events.navigation

import com.bingebot.core.events.EventChannel
import com.bingebot.core.events.EventChannelBase

interface NavigationEventChannel : EventChannel<NavigationEvent>

class NavigationEventChannelImpl : EventChannelBase<NavigationEvent>(), NavigationEventChannel