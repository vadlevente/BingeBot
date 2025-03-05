package com.vadlevente.bingebot.core.ui.composables

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import com.vadlevente.bingebot.core.UIText
import com.vadlevente.bingebot.core.asString

@Composable
fun BBOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: UIText? = null,
    hint: UIText? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onValueChange: (String) -> Unit = {},
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon: @Composable () -> Unit = {},
    maxLines: Int = Int.MAX_VALUE,
) {
    BBOutlinedTextField(
        modifier = modifier,
        value = value,
        label = label?.let {
            {
                Text(
                    text = it.asString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        placeholder = hint?.let {
            { Text(text = it.asString()) }
        },
        visualTransformation = visualTransformation,
        onValueChange = onValueChange,
        isError = isError,
        keyboardOptions = keyboardOptions,
        trailingIcon = trailingIcon,
        maxLines = maxLines,
    )
}

@Composable
private fun BBOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onValueChange: (String) -> Unit = {},
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon: @Composable () -> Unit = {},
    maxLines: Int,
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        label = label,
        placeholder = placeholder,
        visualTransformation = visualTransformation,
        onValueChange = onValueChange,
        isError = isError,
        keyboardOptions = keyboardOptions,
        trailingIcon = trailingIcon,
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = MaterialTheme.colorScheme.onBackground,
            focusedBorderColor = MaterialTheme.colorScheme.onBackground,
            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
            focusedTextColor = MaterialTheme.colorScheme.primary,
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
        ),
        maxLines = maxLines,
    )
}