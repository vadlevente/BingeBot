package com.vadlevente.bingebot.authentication.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vadlevente.bingebot.core.UIText
import com.vadlevente.bingebot.core.asString
import com.vadlevente.bingebot.core.stringOf
import com.vadlevente.bingebot.core.util.Constants.PIN_LENGTH
import com.vadlevente.bingebot.ui.BingeBotTheme

@Composable
fun PinScreen(
    modifier: Modifier = Modifier,
    title: UIText,
    pin: String,
    onPinChanged: (String) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier.padding(bottom = 42.dp),
            text = title.asString(),
            style = MaterialTheme.typography.titleMedium.copy(
                textAlign = TextAlign.Center,
            ),
            color = MaterialTheme.colorScheme.primary
        )
        // Pin Display
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            repeat(PIN_LENGTH) { index ->
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .border(2.dp, BingeBotTheme.colors.highlight, CircleShape)
                        .background(if (index < pin.length) BingeBotTheme.colors.highlight else MaterialTheme.colorScheme.background)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Number Pad
        val digits = (1..9).toList() + listOf(null, 0, "⌫")

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            digits.chunked(3).forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    row.forEach { digit ->
                        if (digit == null) {
                            Spacer(modifier = Modifier.size(56.dp))
                        } else {
                            Button(
                                onClick = {
                                    val newValue = if (digit == "⌫" && pin.isNotEmpty()) {
                                        pin.dropLast(1)
                                    } else if (digit is Int && pin.length < PIN_LENGTH) {
                                        pin + digit.toString()
                                    } else null
                                    newValue?.let {
                                        onPinChanged(it)
                                    }
                                },
                                modifier = Modifier.size(56.dp),
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Text(
                                    text = digit.toString(),
                                    fontSize = 24.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
@Preview
private fun PinScreenPreview() {
    BingeBotTheme {
        PinScreen(
            title = stringOf("Adja meg a PIN kódját"),
            pin = "5674",
            onPinChanged = {},
        )
    }
}