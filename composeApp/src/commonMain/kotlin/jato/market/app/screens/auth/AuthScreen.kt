package jato.market.app.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import compose.icons.FeatherIcons
import compose.icons.feathericons.Eye
import compose.icons.feathericons.EyeOff
import compose.icons.feathericons.Lock
import compose.icons.feathericons.Mail
import compose.icons.feathericons.User
import jato.market.app.data_model.TextModel
import jato.market.app.screens.home.HomeScreen
import jato.market.app.screens.stores.StoresScreen
import jato.market.app.theme.ClickableStyledText
import jato.market.app.theme.EXTRA_LARGE_PADDING
import jato.market.app.theme.HorizontalTextIcon
import jato.market.app.theme.LARGE_PADDING
import jato.market.app.theme.MEDIUM_PADDING
import jato.market.app.theme.SMALL_PADDING
import jatomarket_.composeapp.generated.resources.Res
import jatomarket_.composeapp.generated.resources.market
import org.jetbrains.compose.resources.painterResource

object AuthScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { AuthScreemModel() }
        val navigator = LocalNavigator.currentOrThrow
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            HorizontalTextIcon(
                modifier = Modifier.padding(LARGE_PADDING)
                    .clickable { navigator.push(StoresScreen) }
                    .padding(SMALL_PADDING),
                text = "Jato Market",
                textStyle = MaterialTheme.typography.titleLarge,
                leadingIcon = {
                    Image(
                        modifier = Modifier.size(EXTRA_LARGE_PADDING),
                        painter = painterResource(Res.drawable.market),
                        contentDescription = "market image"
                    )
                },
            )
            when (screenModel.getAuthScreenType) {
                AuthScreenType.Login -> LoginScreen(screenModel)
                AuthScreenType.Register -> RegisterScreen(screenModel)
            }
        }
    }

    @Composable
    private fun LoginScreen(screenModel: AuthScreemModel) {
        val navigator = LocalNavigator.currentOrThrow

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MEDIUM_PADDING)
        ) {
            Text(text = "Welcome Back", style = MaterialTheme.typography.titleMedium)
            TextField(
                value = screenModel.email,
                onValueChange = { screenModel.email = it },
                label = { Text("Email") },
                singleLine = true,
                leadingIcon = { Icon(FeatherIcons.Mail, contentDescription = null) },
                textStyle = MaterialTheme.typography.bodyMedium
            )
            TextField(
                value = screenModel.password,
                onValueChange = { screenModel.password = it },
                label = { Text("Password") },
                leadingIcon = { Icon(FeatherIcons.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { screenModel.hidePassword = !screenModel.hidePassword }) {
                        Icon(
                            if (screenModel.hidePassword) FeatherIcons.EyeOff
                            else FeatherIcons.Eye,
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (screenModel.hidePassword) PasswordVisualTransformation() else VisualTransformation.None,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium
            )
            Button(onClick = {
                screenModel.logIn(onComplete = { navigator.replace(HomeScreen) })
            }) {
                Text("Login", style = MaterialTheme.typography.titleMedium)
            }
            ClickableStyledText(
                texts = listOf(
                    TextModel("Don't have an account? "),
                    TextModel(
                        text = "Register",
                        style = MaterialTheme.typography.titleMedium,
                        onClick = { screenModel.getAuthScreenType = AuthScreenType.Register }
                    )
                )
            )
        }
    }

    @Composable
    private fun RegisterScreen(screenModel: AuthScreemModel) {
        val navigator = LocalNavigator.currentOrThrow

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MEDIUM_PADDING)
        ) {
            Text(text = "Hello Visitor", style = MaterialTheme.typography.titleMedium)
            TextField(
                value = screenModel.firstName,
                onValueChange = { screenModel.firstName = it },
                label = { Text("First Name") },
                singleLine = true,
                leadingIcon = { Icon(FeatherIcons.User, contentDescription = null) },
            )
            TextField(
                value = screenModel.lastName,
                onValueChange = { screenModel.lastName = it },
                label = { Text("Last Name") },
                singleLine = true,
                leadingIcon = { Icon(FeatherIcons.User, contentDescription = null) },
            )
            TextField(
                value = screenModel.email,
                onValueChange = { screenModel.email = it },
                label = { Text("Email") },
                singleLine = true,
                leadingIcon = { Icon(FeatherIcons.Mail, contentDescription = null) },
            )
            TextField(
                value = screenModel.password,
                onValueChange = { screenModel.password = it },
                label = { Text("Password") },
                visualTransformation = if (screenModel.hidePassword) PasswordVisualTransformation() else VisualTransformation.None,
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { screenModel.hidePassword = !screenModel.hidePassword }) {
                        Icon(
                            if (screenModel.hidePassword) FeatherIcons.EyeOff
                            else FeatherIcons.Eye,
                            contentDescription = null
                        )
                    }
                },
                leadingIcon = { Icon(FeatherIcons.Lock, contentDescription = null) },
            )

            Button(onClick = {
                screenModel.signUp(onComplete = { navigator.replace(HomeScreen) })
            }) {
                Text("Register", style = MaterialTheme.typography.titleMedium)
            }
            ClickableStyledText(
                texts = listOf(
                    TextModel("If you already have an account? "),
                    TextModel(
                        text = "Login",
                        style = MaterialTheme.typography.titleMedium,
                        onClick = { screenModel.getAuthScreenType = AuthScreenType.Login }
                    )
                )
            )
        }
    }
}
