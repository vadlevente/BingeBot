package com.vadlevente.bingebot.common.ui.composables

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import com.vadlevente.bingebot.common.ui.util.asString
import com.vadlevente.bingebot.core.ui.UIText

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
    var state by remember {
        mutableStateOf(TextFieldValue(value, TextRange(value.length)))
    }
    BBOutlinedTextField(
        modifier = modifier,
        value = state,
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
        onValueChange = {
            state = it
            onValueChange(it.text)
        },
        isError = isError,
        keyboardOptions = keyboardOptions,
        trailingIcon = trailingIcon,
        maxLines = maxLines,
    )
}

@Composable
fun BBOutlinedTextField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onValueChange: (TextFieldValue) -> Unit = {},
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
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedTextColor = MaterialTheme.colorScheme.primary,
            unfocusedTextColor = MaterialTheme.colorScheme.primary,
            errorBorderColor = MaterialTheme.colorScheme.error,
            errorLabelColor = MaterialTheme.colorScheme.error,
        ),
        maxLines = maxLines,
    )
}