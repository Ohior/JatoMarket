package jato.market.app.screens.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import jato.app.jato_utils.JDevice
import jato.app.jato_utils.calculateRatio
import jato.app.jato_utils.rememberJDevice
import jato.app.jato_utils.roundToDecimalPlaces
import jato.market.app.theme.SMALL_PADDING
import jato.market.app.theme.rotateTo
import jato.market.app.utils.Tools
import jatomarket_.composeapp.generated.resources.Res
import jatomarket_.composeapp.generated.resources.arrow_up
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

class MapScreen(private val userDocumentId: String) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = rememberScreenModel { MapScreenModel(userDocumentId) }
        Scaffold(
            Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text("🗺 View Map") },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceBright),
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(FeatherIcons.ArrowLeft, "Go Back")
                        }
                    },
                )
            }
        ) { pv ->
            val jDevice = rememberJDevice()
            Box(Modifier.padding(pv).padding(SMALL_PADDING)) {
                when (jDevice) {
                    is JDevice.Portrait -> {
                        PortraitDisplay(
                            modifier = Modifier.fillMaxSize(),
                            jDevice = jDevice
                        )
                    }

                    else -> {
                        LandscapeDisplay(
                            modifier = Modifier.fillMaxSize(),
                            jDevice = jDevice
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun PortraitDisplay(modifier: Modifier, jDevice: JDevice) {
        val scope = rememberCoroutineScope()
        val scrollState = rememberLazyListState()
        LazyRow(
            state = scrollState,
            modifier = modifier.then(
                Modifier.draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        scope.launch {
                            scrollState.scrollBy(-delta)
                        }
                    },
                ),
            )
        ) {
            items(1) {
                if (it == 0) {
                    CompassAreaDisplay(
                        modifier = Modifier.fillMaxHeight(),
                        size = jDevice.width - (SMALL_PADDING.value.toInt() * 2)
                    )
                } else {
                    MapAreaDisplay(
                        modifier = Modifier.fillMaxHeight()
                            .size(width = jDevice.width.dp, height = jDevice.height.dp)
                    )
                }
            }
        }
    }

    @Composable
    private fun LandscapeDisplay(modifier: Modifier, jDevice: JDevice) {
        val (first, last) = remember(jDevice) { jDevice.width.calculateRatio(40, 60) }
        Row(
            modifier,
            horizontalArrangement = Arrangement.Center
//            horizontalArrangement = Arrangement.spacedBy(SMALL_PADDING)
        ) {
            CompassAreaDisplay(
                modifier = Modifier.width(first.dp).fillMaxHeight(),
                size = first
            )
//            VerticalDivider()
//            MapAreaDisplay(
//                modifier = Modifier.width(last.dp).fillMaxHeight()
//            )
        }
    }

    @Composable
    private fun MapAreaDisplay(modifier: Modifier) {
        Box(modifier, contentAlignment = androidx.compose.ui.Alignment.Center) {
            Text("Map Area")
        }
    }

    @Composable
    private fun CompassAreaDisplay(modifier: Modifier, size: Int) {
        val angle = Tools.getAngle(0.0, 0.0, 45.0, 45.0)
        Column(modifier, horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(size.dp)
                    .rotateTo(angle.toFloat(), true)
                    .border(
                        width = SMALL_PADDING / 2,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = CircleShape
                    ).padding(SMALL_PADDING),
                painter = painterResource(Res.drawable.arrow_up),
                contentScale = ContentScale.FillBounds,
                contentDescription = "Compass",
            )
            Text("${angle.roundToDecimalPlaces(1)}")
            Text("longitude and latitude")
            Text("store address")
            Text("user address")
        }
    }
}





