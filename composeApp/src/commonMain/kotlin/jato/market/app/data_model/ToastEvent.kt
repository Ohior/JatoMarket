package jato.market.app.data_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ToastEvent {
    var showToast by mutableStateOf(false)
}