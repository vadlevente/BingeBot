package com.bingebot.core.ui.composables

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import com.bingebot.core.UIText
import com.bingebot.core.asString

@Composable
fun BBOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: UIText? = null,
    hint: UIText? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onValueChange: (String) -> Unit = {},
    isError: Boolean = false,
) {
    BBOutlinedTextField(
        modifier = modifier,
        value = value,
        label = label?.let {
            { Text(text = it.asString()) }
        },
        placeholder = hint?.let {
            { Text(text = it.asString()) }
        },
        visualTransformation = visualTransformation,
        onValueChange = onValueChange,
        isError = isError,
    )
}

@Composable
fun BBOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onValueChange: (String) -> Unit = {},
    isError: Boolean = false,
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        label = label,
        placeholder = placeholder,
        visualTransformation = visualTransformation,
        onValueChange = onValueChange,
        isError = isError,
    )
}