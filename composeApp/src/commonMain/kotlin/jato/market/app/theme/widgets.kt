package jato.market.app.theme

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import jato.market.app.data_model.ComposeWidget
import jato.market.app.data_model.TextModel
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
    onClick: () -> Unit,
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

//
//@Composable
//fun Modifier.createShimmer(colors: List<Color>): Modifier = composed {
//    var size by remember { mutableStateOf(IntSize.Zero) }
//    val transition = rememberInfiniteTransition()
//    val startOffset by transition.animateFloat(
//        initialValue = -2 * size.width.toFloat(),
//        targetValue = 2 * size.width.toFloat(),
//        animationSpec = infiniteRepeatable(
//            animation = tween(1000),
//            repeatMode = RepeatMode.Reverse
//        )
//    )
//    background(
//        brush = Brush.linearGradient(
//            colors = colors,
//            start = Offset(startOffset, 0f),
//            end = Offset(startOffset + size.width, size.height.toFloat())
//        )
//    )
//        .onGloballyPositioned { size = it.size }
//}
//
//@Composable
//fun Modifier.drawUnderLine(color: Color, thickness: Dp = PixelDensity.verySmall): Modifier = composed {
//    drawBehind {
//        val underlineThickness = thickness.toPx() // Thickness of the underline
//        // Calculate the start and end positions of the underline
//        val startX = 0f
//        val endX = size.width
//        val baselineY = size.height// + 10
//        // Draw the underline
//        drawLine(
//            color = color, // Color of the underline
//            start = Offset(startX, baselineY),
//            end = Offset(endX, baselineY),
//            strokeWidth = underlineThickness
//        )
//    }
//}
//

@Composable
fun AutoSwitcher(images: List<ComposeWidget>) {
    // Remember the current image index
    var currentIndex by remember { mutableStateOf(0) }

    // Update the image index every 2 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000L) // Wait for 10 seconds
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

//@Composable
//fun Modifier.scrollScreen(
//    orientation: Orientation,
//    scope: CoroutineScope
//): Modifier {
//    return this
//        .draggable(
//            orientation = orientation,
//            state = rememberDraggableState { delta ->
//                println("delta: $delta")
////                offset.value += delta
//                scope.launch {
//                    scrollState.scrollBy(-500f)
//                }
//            }
//        )
////        .draggable(
////        orientation = orientation,
////        state = rememberDraggableState { delta ->
////            scope.launch {
////                scrollState.scrollBy(-delta)
////            }
////        },
////    )
//}
