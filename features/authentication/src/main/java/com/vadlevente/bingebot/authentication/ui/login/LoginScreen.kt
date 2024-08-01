package com.vadlevente.bingebot.authentication.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadlevente.bingebot.authentication.R
import com.vadlevente.bingebot.authentication.ui.login.LoginViewModel.ViewState
import com.vadlevente.bingebot.core.stringOf
import com.vadlevente.bingebot.core.ui.composables.BBOutlinedButton
import com.vadlevente.bingebot.core.ui.composables.BBOutlinedTextField
import com.vadlevente.bingebot.core.ui.composables.PasswordTrailingIcon
import com.vadlevente.bingebot.ui.BingeBotTheme
import com.vadlevente.bingebot.ui.backgroundColor
import com.vadlevente.bingebot.ui.link
import com.vadlevente.bingebot.ui.margin16
import com.vadlevente.bingebot.ui.margin8
import com.vadlevente.bingebot.ui.pageTitle

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    LoginScreenComponent(
        state,
        viewModel::onEmailChanged,
        viewModel::onPasswordChanged,
        viewModel::onSubmit,
        viewModel::onNavigateToRegistration,
    )
}

@Composable
fun LoginScreenComponent(
    state: ViewState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onSubmit: () -> Unit,
    onNavigateToRegistration: () -> Unit,
) {
    var passwordVisible by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
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
                text = stringResource(id = R.string.loginTitle),
                textAlign = TextAlign.Center,
                style = pageTitle,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.7f)
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
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                onValueChange = onPasswordChanged,
                trailingIcon = {
                    PasswordTrailingIcon(isPasswordVisible = passwordVisible) {
                        passwordVisible = !passwordVisible
                    }
                }
            )
            Spacer(modifier = Modifier.fillMaxHeight(.2f))
            BBOutlinedButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = margin8),
                enabled = state.isSubmitEnabled,
                onClick = onSubmit,
                text = stringResource(id = R.string.loginSubmitButtonTitle)
            )
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        onNavigateToRegistration()
                    },
                text = stringResource(id = R.string.loginNavigateToRegistrationTitle),
                style = link,
            )
        }
    }

}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun LoginScreenPreview() {
    BingeBotTheme {
        LoginScreenComponent(
            ViewState("email", "password", true), {}, {}, {}, {}
        )
    }
}