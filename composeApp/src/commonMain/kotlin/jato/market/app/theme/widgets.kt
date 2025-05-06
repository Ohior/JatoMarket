package jato.market.app.theme

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.Popup
import jato.market.app.data_model.ComposeWidget
import jato.market.app.data_model.TextModel
import jato.market.app.data_model.ToastEvent
import kotlinx.coroutines.delay

@Composable
fun HorizontalTextIcon(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SMALL_PADDING)
    ) {
        leadingIcon?.invoke()
        Text(text, style = textStyle)
        trailingIcon?.invoke()
    }
}

@Composable
fun VerticalTextIcon(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(SMALL_PADDING)
    ) {
        leadingIcon?.invoke()
        Text(text, style = textStyle)
        trailingIcon?.invoke()
    }
}

@Composable
fun ClickableStyledText(
    modifier: Modifier = Modifier,
    texts: List<TextModel>,
) {
    val annotatedText = buildAnnotatedString {
        for ((index, text) in texts.withIndex()) {
            if (text.onClick != null) {
                withLink(
                    LinkAnnotation.Clickable(
                        "${text.text}_$index",
                        TextLinkStyles(text.style.toSpanStyle()),
                        linkInteractionListener = {
                            text.onClick.invoke()
                        }
                    ),
                ) {
                    append(text.text)
                }
            } else {
                withStyle(text.style.toSpanStyle()) {
                    append(text.text)
                }
            }
        }
    }

    Text(
        modifier = modifier,
        text = annotatedText
    )
}

@Composable
fun ExpandableDropdown(
    content: @Composable () -> Unit,
    onClick: () -> Unit = {},
    items: List<ComposeWidget>,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(Modifier.clickable(onClick = {
        expanded = true
        onClick()
    })) { content() }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        for (item in items) {
            item.content()
        }
    }
}


@Composable
fun Modifier.createShimmer(colors: List<Color>, duration: Int = 1000): Modifier = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val transition = rememberInfiniteTransition()
    val startOffset by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(duration),
            repeatMode = RepeatMode.Reverse
        )
    )
    background(
        brush = Brush.linearGradient(
            colors = colors,
            start = Offset(startOffset, 0f),
            end = Offset(startOffset + size.width, size.height.toFloat())
        )
    )
        .onGloballyPositioned { size = it.size }
}

@Composable
fun Modifier.drawUnderLine(color: Color, thickness: Float): Modifier = composed {
    drawBehind {
        // Calculate the start and end positions of the underline
        val startX = 0f
        val endX = size.width
        val baselineY = size.height// + 10
        // Draw the underline
        drawLine(
            color = color, // Color of the underline
            start = Offset(startX, baselineY),
            end = Offset(endX, baselineY),
            strokeWidth = thickness
        )
    }
}


@Composable
fun AutoSwitcher(images: List<ComposeWidget>, millisSec: Long = 50000L) {
    // Remember the current image index
    var currentIndex by remember { mutableStateOf(0) }

    // Update the image index every 2 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(millisSec) // Wait for 10 seconds
            currentIndex = (currentIndex + 1) % images.size // Cycle through the images
        }
    }

    // Display the current image
    AnimatedContent(
        targetState = currentIndex,
        transitionSpec = {
            slideInHorizontally { height -> height } + fadeIn() togetherWith
                    slideOutHorizontally { height -> -height } + fadeOut()
        }) {
        images[currentIndex].content()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToastLayout( 
    toastAlignment: Alignment = Alignment.Center,
    toastDelay: Long = 2000,
    toastContent: @Composable () -> Unit,
    content: @Composable ToastEvent.() -> Unit,
) {
    val toastEvent = remember { ToastEvent() }
    LaunchedEffect(toastEvent.showToast) {
        delay(toastDelay)
        toastEvent.showToast = false
    }
    toastEvent.content()
    if (toastEvent.showToast) {
        Popup{
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = toastAlignment
            ) { toastContent() }
        }
    }

}

@Composable
fun Modifier.continuousRotate(durationMillis: Int = 1000): Modifier {
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    return this.then(Modifier.rotate(angle))
}


fun Modifier.rotateTo(
    targetAngle: Float,
    trigger: Boolean,
    durationMillis: Int = 1000
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "rotateTo"
        properties["targetAngle"] = targetAngle
        properties["trigger"] = trigger
    }
) {
    var startAnimation by remember { mutableStateOf(false) }
    val angle by animateFloatAsState(
        targetValue = if (startAnimation) targetAngle else 0f,
        animationSpec = tween(durationMillis),
        label = "rotateTo"
    )

    LaunchedEffect(trigger) {
        if (trigger) startAnimation = true
    }

    this.then(Modifier.rotate(angle))
}

//val clipboardManager = LocalClipboardManager.current
//clipboardManager.setText(
//annotatedString = buildAnnotatedString {
//    append(text = it.second)
//}
//)