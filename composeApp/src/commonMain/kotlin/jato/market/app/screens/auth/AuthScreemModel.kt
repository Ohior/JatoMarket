package jato.market.app.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel

enum class AuthScreenType {
    Login,
    Register
}

class AuthScreemModel : ScreenModel {
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var hidePassword by mutableStateOf(true)

    var getAuthScreenType by mutableStateOf(AuthScreenType.Login)

    fun logIn(onComplete: () -> Unit) {
        onComplete()
    }

    fun signUp(onComplete: () -> Unit) {
        onComplete()
    }
}