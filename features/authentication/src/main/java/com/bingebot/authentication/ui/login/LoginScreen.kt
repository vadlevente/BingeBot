package com.bingebot.authentication.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.bingebot.authentication.ui.login.LoginViewModel.ViewState
import com.bingebot.core.R
import com.bingebot.ui.BingeBotTheme
import com.bingebot.ui.margin16

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    LoginScreenComponent(
        state,
        viewModel::onEmailChanged,
        viewModel::onPasswordChanged,
        viewModel::onSubmit,
    )
}

@Composable
fun LoginScreenComponent(
    state: ViewState,
    onEmailChanged: (String) -> Unit = {},
    onPasswordChanged: (String) -> Unit = {},
    onSubmit: () -> Unit = {},
) {
    Text("asdasdasdasd")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(margin16)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.4f),
            value = state.email,
            onValueChange = onEmailChanged
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.8f),
            value = state.email,
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = onPasswordChanged
        )
        OutlinedButton(
            onClick = onSubmit
        ) {
            Text(text = stringResource(id = R.string.loginSubmitButtonTitle))
        }
    }

}

@Composable
@Preview
fun LoginScreenPreview() {
    BingeBotTheme {
        LoginScreenComponent(
            ViewState("email", "password", true),
        )
    }
}