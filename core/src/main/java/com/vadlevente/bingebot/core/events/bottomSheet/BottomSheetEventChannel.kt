package com.vadlevente.bingebot.core.events.bottomSheet

import com.vadlevente.bingebot.core.events.EventChannel
import com.vadlevente.bingebot.core.events.EventChannelBase
import javax.inject.Inject

interface BottomSheetEventChannel : EventChannel<BottomSheetEvent>

class BottomSheetEventChannelImpl @Inject constructor(

) : EventChannelBase<BottomSheetEvent>(), BottomSheetEventChannel