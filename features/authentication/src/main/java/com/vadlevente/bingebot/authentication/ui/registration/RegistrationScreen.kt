package com.vadlevente.bingebot.authentication.ui.registration

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vadlevente.bingebot.authentication.R
import com.vadlevente.bingebot.authentication.R.string
import com.vadlevente.bingebot.authentication.ui.registration.RegistrationViewModel.ViewState
import com.vadlevente.bingebot.common.ui.composables.BBOutlinedButton
import com.vadlevente.bingebot.common.ui.composables.BBOutlinedTextField
import com.vadlevente.bingebot.common.ui.composables.PasswordTrailingIcon
import com.vadlevente.bingebot.core.ui.stringOf
import com.vadlevente.bingebot.ui.BingeBotTheme
import com.vadlevente.bingebot.ui.margin16
import com.vadlevente.bingebot.ui.margin8
import kotlinx.coroutines.launch

@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    val state by viewModel.state.collectAsStateWithLifecycle()
    RegistrationScreenComponent(
        state = state,
        onEmailChanged = viewModel::onEmailChanged,
        onPasswordChanged = viewModel::onPasswordChanged,
        onConfirmedPasswordChanged = viewModel::onConfirmedPasswordChanged,
        onSubmit = viewModel::onSubmit,
        onNavigateToLogin = viewModel::onNavigateToLogin,
        onBackPressed = {
            coroutineScope.launch {
                viewModel.showExitConfirmation()
            }
        },
    )
}

@Composable
fun RegistrationScreenComponent(
    state: ViewState,
    onEmailChanged: (String) -> Unit = {},
    onPasswordChanged: (String) -> Unit = {},
    onConfirmedPasswordChanged: (String) -> Unit = {},
    onSubmit: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(margin16)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(.8f)
                .align(Alignment.CenterHorizontally)
                .weight(.3f)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(id = R.string.registrationTitle),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.7f)
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(.1f))
            BBOutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.email,
                label = stringOf(string.registrationEmailLabel),
                hint = stringOf(string.registrationEmailLabel),
                onValueChange = onEmailChanged,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
            )
            Spacer(modifier = Modifier.fillMaxHeight(.1f))
            BBOutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.password,
                label = stringOf(string.registrationPasswordLabel),
                hint = stringOf(string.registrationPasswordLabel),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                onValueChange = onPasswordChanged,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                trailingIcon = {
                    PasswordTrailingIcon(isPasswordVisible = passwordVisible) {
                        passwordVisible = !passwordVisible
                    }
                }
            )
            Spacer(modifier = Modifier.fillMaxHeight(.1f))
            BBOutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.passwordConfirmed,
                label = stringOf(string.registrationConfirmPasswordLabel),
                hint = stringOf(string.registrationConfirmPasswordLabel),
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                onValueChange = onConfirmedPasswordChanged,
                isError = state.passwordsDoNotMatch,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                trailingIcon = {
                    PasswordTrailingIcon(isPasswordVisible = confirmPasswordVisible) {
                        confirmPasswordVisible = !confirmPasswordVisible
                    }
                }
            )
            if (state.passwordsDoNotMatch) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.registrationPasswordsDoNotMatchLabel),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
            Spacer(modifier = Modifier.fillMaxHeight(.1f))
            BBOutlinedButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = margin8),
                enabled = state.isSubmitEnabled,
                onClick = onSubmit,
                text = stringResource(id = string.registrationSubmitButtonTitle)
            )
            Spacer(modifier = Modifier.fillMaxHeight(.1f))
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        onNavigateToLogin()
                    },
                text = stringResource(id = R.string.registrationNavigateToLoginTitle),
                style = MaterialTheme.typography.bodySmall,
                color = BingeBotTheme.colors.highlight,
            )
        }
    }

    BackHandler {
        onBackPressed()
    }

}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun LoginScreenPreview() {
    BingeBotTheme {
        RegistrationScreenComponent(
            ViewState("email", "password", "password", true, false),
        )
    }
}