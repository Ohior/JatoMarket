package jato.market.app.data_model

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle

data class TextModel(
    val text: String,
    val style: TextStyle = TextStyle.Default,
    val onClick: (() -> Unit)? = null
)

data class ComposeWidget(val content: @Composable () -> Unit)
