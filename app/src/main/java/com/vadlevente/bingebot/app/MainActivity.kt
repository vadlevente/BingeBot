package com.vadlevente.bingebot.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.vadlevente.bingebot.core.events.navigation.NavigationEvent.TopNavigationEvent.ExitApplication
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.ui.BingeBotTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navigationEventChannel: NavigationEventChannel

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BingeBotTheme {
                NavigationHost(
                    navigationEventChannel = navigationEventChannel,
                )
            }
        }
        lifecycleScope.launch {
            navigationEventChannel.events.collect {
                if (it is ExitApplication) {
                    finish()
                }
            }
        }
    }
}