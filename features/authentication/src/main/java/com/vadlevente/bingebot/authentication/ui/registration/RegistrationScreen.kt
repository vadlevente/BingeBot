package com.vadlevente.bingebot.authentication.ui.registration

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadlevente.bingebot.authentication.ui.registration.RegistrationViewModel.ViewState
import com.vadlevente.bingebot.core.R
import com.vadlevente.bingebot.core.stringOf
import com.vadlevente.bingebot.core.ui.composables.BBOutlinedTextField
import com.vadlevente.bingebot.ui.BingeBotTheme
import com.vadlevente.bingebot.ui.errorLabel
import com.vadlevente.bingebot.ui.margin16
import com.vadlevente.bingebot.ui.margin8
import com.vadlevente.bingebot.ui.pageTitle

@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    RegistrationScreenComponent(
        state,
        viewModel::onEmailChanged,
        viewModel::onPasswordChanged,
        viewModel::onConfirmedPasswordChanged,
        viewModel::onSubmit,
    )
}

@Composable
fun RegistrationScreenComponent(
    state: ViewState,
    onEmailChanged: (String) -> Unit = {},
    onPasswordChanged: (String) -> Unit = {},
    onConfirmedPasswordChanged: (String) -> Unit = {},
    onSubmit: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(margin16)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(.8f)
                .align(Alignment.CenterHorizontally)
                .weight(.4f)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(id = R.string.registrationTitle),
                textAlign = TextAlign.Center,
                style = pageTitle,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.6f)
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(.1f))
            BBOutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.email,
                label = stringOf(R.string.registrationEmailLabel),
                hint = stringOf(R.string.registrationEmailLabel),
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
                label = stringOf(R.string.registrationPasswordLabel),
                hint = stringOf(R.string.registrationPasswordLabel),
                visualTransformation = PasswordVisualTransformation(),
                onValueChange = onPasswordChanged,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            )
            Spacer(modifier = Modifier.fillMaxHeight(.1f))
            BBOutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.passwordConfirmed,
                label = stringOf(R.string.registrationConfirmPasswordLabel),
                hint = stringOf(R.string.registrationConfirmPasswordLabel),
                visualTransformation = PasswordVisualTransformation(),
                onValueChange = onConfirmedPasswordChanged,
                isError = state.passwordsDoNotMatch,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            )
            if (state.passwordsDoNotMatch) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.registrationPasswordsDoNotMatchLabel),
                    style = errorLabel,
                )
            }
            Spacer(modifier = Modifier.fillMaxHeight(.1f))
            OutlinedButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = margin8),
                enabled = state.isSubmitEnabled,
                onClick = onSubmit
            ) {
                Text(text = stringResource(id = R.string.registrationSubmitButtonTitle))
            }
        }
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