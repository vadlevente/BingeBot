package com.bingebot.authentication.ui.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.bingebot.authentication.ui.login.LoginViewModel.ViewState
import com.bingebot.core.R
import com.bingebot.core.stringOf
import com.bingebot.core.ui.composables.BBOutlinedTextField
import com.bingebot.ui.BingeBotTheme
import com.bingebot.ui.margin16
import com.bingebot.ui.margin8
import com.bingebot.ui.pageTitle

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
                text = stringResource(id = R.string.loginTitle),
                textAlign = TextAlign.Center,
                style = pageTitle,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.6f)
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(.2f))
            BBOutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.email,
                label = stringOf(R.string.loginEmailLabel),
                hint = stringOf(R.string.loginEmailLabel),
                onValueChange = onEmailChanged,
            )
            Spacer(modifier = Modifier.fillMaxHeight(.2f))
            BBOutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.password,
                label = stringOf(R.string.loginPasswordLabel),
                hint = stringOf(R.string.loginPasswordLabel),
                visualTransformation = PasswordVisualTransformation(),
                onValueChange = onPasswordChanged,
            )
            Spacer(modifier = Modifier.fillMaxHeight(.2f))
            OutlinedButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = margin8),
                enabled = state.isSubmitEnabled,
                onClick = onSubmit
            ) {
                Text(text = stringResource(id = R.string.loginSubmitButtonTitle))
            }
        }
    }

}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun LoginScreenPreview() {
    BingeBotTheme {
        LoginScreenComponent(
            ViewState("email", "password", true),
        )
    }
}