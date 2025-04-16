package jato.market.app.screens.stores

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import compose.icons.FeatherIcons
import compose.icons.feathericons.Menu
import compose.icons.feathericons.MoreVertical
import compose.icons.feathericons.User
import jato.app.jato_utils.JDevice
import jato.app.jato_utils.rememberJDevice
import jato.market.app.data_model.ComposeWidget
import jato.market.app.screens.auth.AuthScreen
import jato.market.app.screens.profile.ProfileScreen
import jato.market.app.screens.store.StoreScreen
import jato.market.app.theme.ExpandableDropdown
import jato.market.app.theme.HorizontalTextIcon
import jato.market.app.theme.LARGE_PADDING
import jato.market.app.theme.MEDIUM_PADDING
import jato.market.app.theme.SMALL_PADDING
import jatomarket_.composeapp.generated.resources.Res
import jatomarket_.composeapp.generated.resources.market
import org.jetbrains.compose.resources.painterResource

private val items = List(100) { "Item #$it" }

object StoresScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val device = rememberJDevice()
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = rememberScreenModel { StoresScreenModel() }

        Scaffold(
            Modifier.fillMaxSize(),
            topBar = {
                val jsonData by screenModel.jsonDatabase.getUserDataFlow().collectAsState(null)
                TopAppBar(
                    title = { Text("Stores") },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceBright),
                    actions = {
                        IconButton(onClick = {
                            if (jsonData == null) {
                                navigator.replace(AuthScreen)
                            } else {
                                navigator.replace(ProfileScreen(jsonData))
                            }
                        }) { Icon(FeatherIcons.User, "User icons") }
                        ExpandableDropdown(
                            content = {
                                Icon(FeatherIcons.MoreVertical, "Drop down menu")
                            },
                            items = listOf(
                                ComposeWidget {
                                    HorizontalTextIcon(
                                        modifier = Modifier.padding(horizontal = SMALL_PADDING)
                                            .clickable { navigator.push(AuthScreen) },
                                        text = "Authentication", leadingIcon = {
                                            Icon(
                                                FeatherIcons.Menu,
                                                contentDescription = null
                                            )
                                        })
                                }
                            )
                        )
                    }
                )
            }
        ) { pv ->
            Column(Modifier.padding(pv).fillMaxSize()) {
                when (device) {
                    is JDevice.Portrait -> {
                        Column(
                            Modifier.fillMaxWidth().background(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(
                                    bottomStart = SMALL_PADDING,
                                    bottomEnd = SMALL_PADDING
                                )
                            ).padding(MEDIUM_PADDING),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            TextField(
                                value = screenModel.searchQuery,
                                onValueChange = { screenModel.searchQuery = it },
                                singleLine = true,
                                textStyle = MaterialTheme.typography.bodyMedium,
                                placeholder = {
                                    Text(
                                        text = "eg name, location",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            )
                            Spacer(Modifier.height(LARGE_PADDING))
                            HorizontalTextIcon(
                                text = "Your location",
                                textStyle = MaterialTheme.typography.bodyMedium,
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.LocationOn,
                                        contentDescription = null
                                    )
                                }
                            )
                        }

                    }

                    else -> {
                        Row(
                            Modifier.fillMaxWidth().background(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(
                                    bottomStart = SMALL_PADDING,
                                    bottomEnd = SMALL_PADDING
                                )
                            ).padding(MEDIUM_PADDING),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = screenModel.searchQuery,
                                onValueChange = { screenModel.searchQuery = it },
                                textStyle = MaterialTheme.typography.bodyMedium,
                                placeholder = { Text("Search for stores by name or location") }
                            )
                            Spacer(Modifier.width(LARGE_PADDING))
                            HorizontalTextIcon(
                                text = "Your location",
                                textStyle = MaterialTheme.typography.bodyMedium,
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.LocationOn,
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                    }
                }
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Adaptive(300.dp),// .Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalItemSpacing = 8.dp,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items) { item ->
                        StoreItemDisplay(navigator, item)
                    }
                }
            }
        }
    }

    @Composable
    private fun StoreItemDisplay(navigator: Navigator, item: String) {
        ListItem(
            modifier = Modifier
                .width(width = 300.dp)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.inversePrimary,
                    MaterialTheme.shapes.medium
                )
                .clickable {
                    navigator.push(
                        StoreScreen(item, item)
                    )
                }
                .padding(SMALL_PADDING),
            headlineContent = {
                Text(
                    "Store title and name $item",
                    style = MaterialTheme.typography.titleMedium
                )
            },
            supportingContent = {
                Text(
                    "Store description and details $item",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            leadingContent = {
                Image(
                    painter = painterResource(Res.drawable.market),
                    contentDescription = "store image"
                )
            }
        )
    }


}