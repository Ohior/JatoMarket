package jato.market.app.screens.store

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import compose.icons.feathericons.Hash
import compose.icons.feathericons.Plus
import compose.icons.feathericons.ShoppingCart
import jato.app.jato_utils.JDevice
import jato.app.jato_utils.getIfNotNull
import jato.app.jato_utils.ifNotNull
import jato.app.jato_utils.rememberJDevice
import jato.market.app.data_model.ComposeWidget
import jato.market.app.data_model.ProductModel
import jato.market.app.data_model.StoreModel
import jato.market.app.screens.profile.ProfileScreen
import jato.market.app.theme.AutoSwitcher
import jato.market.app.theme.EXTRA_LARGE_PADDING
import jato.market.app.theme.HorizontalTextIcon
import jato.market.app.theme.SMALL_PADDING
import jato.market.app.theme.createShimmer
import jato.market.app.theme.drawUnderLine
import jatomarket_.composeapp.generated.resources.Res
import jatomarket_.composeapp.generated.resources.market
import org.jetbrains.compose.resources.painterResource


class StoreScreen(private val storeDocumentId: String, private val userDocumentId: String) :
    Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = rememberScreenModel { StoreScreenModel(userDocumentId, storeDocumentId) }
        val device = rememberJDevice()

        Scaffold(
            Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text("${screenModel.user.firstName} ${screenModel.user.lastName}") },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceBright),
                    actions = {
                        CartDisplay(
                            screenModel.boughtProduct
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            if (screenModel.storeScreenType !is StoreScreenType.Products) {
                                screenModel.storeScreenType = StoreScreenType.Products
                            } else {
                                navigator.pop()
                            }
                        }) {
                            Icon(FeatherIcons.ArrowLeft, "Go Back")
                        }
                    },
                )
            },
            floatingActionButton = {
                screenModel.user.storeDocumentId.ifNotNull {
                    SmallFloatingActionButton(
                        onClick = {
                            // add product to store
                        }) {
                        HorizontalTextIcon(
                            modifier = Modifier.padding(SMALL_PADDING),
                            text = "add product",
                            leadingIcon = {
                                Icon(FeatherIcons.Plus, contentDescription = "add product to store")
                            }
                        )
                    }
                }
            }
        ) { pv ->
            Column(
                Modifier.padding(pv).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (device) {
                    is JDevice.Portrait -> {
                        AutoSwitcher(
                            millisSec = 3000L,
                            images = listOf(
                                ComposeWidget {
                                    StoreOwnerDetails(
                                        storeModel = screenModel.store,
                                        modifier = Modifier.weight(1f)
                                            .height(EXTRA_LARGE_PADDING * 2f)
                                            .clickable {
                                                navigator.push(ProfileScreen(screenModel.user.documentId))
                                            },
                                    )
                                },
                                ComposeWidget {
                                    BonanzaDetails(
                                        modifier = Modifier.weight(1f)
                                            .height(EXTRA_LARGE_PADDING * 2f),
                                    )
                                }
                            )
                        )
                    }

                    else -> {
                        val halfWidth = remember(device.width) { device.width / 2 }
                        Row(
                            Modifier.fillMaxWidth().background(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(
                                    bottomStart = SMALL_PADDING,
                                    bottomEnd = SMALL_PADDING
                                )
                            ),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            StoreOwnerDetails(
                                storeModel = screenModel.store,
                                modifier = Modifier
                                    .size(halfWidth.dp, EXTRA_LARGE_PADDING * 2)
                                    .clickable { navigator.push(ProfileScreen(screenModel.user.documentId)) }
                            )
                            Spacer(Modifier.width(SMALL_PADDING))
                            BonanzaDetails(
                                Modifier
                                    .size(halfWidth.dp, EXTRA_LARGE_PADDING * 2)
                            )
                        }
                    }
                }
                when (screenModel.storeScreenType) {
                    is StoreScreenType.Product -> {
                        DisplayProducts((screenModel.storeScreenType as StoreScreenType.Product).product)
                    }

                    StoreScreenType.Products -> {
                        LazyVerticalStaggeredGrid(
                            columns = StaggeredGridCells.Adaptive(300.dp),// .Fixed(2),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            verticalItemSpacing = 8.dp,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(items = screenModel.user.store.getIfNotNull(emptyList()) { it.products }) { item ->
                                StoreItemDisplay(item) {
                                    screenModel.storeScreenType = StoreScreenType.Product(item)
                                }
                            }
                        }
                    }

                }
            }
        }
    }


    @Composable
    private fun DisplayProducts(product: ProductModel) {
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            product.productImageUrls?.forEach { p ->
                Box(
                    modifier = Modifier.height(EXTRA_LARGE_PADDING * 5),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    CoilImage(
                        modifier = Modifier.padding(SMALL_PADDING),
                        imageModel = { p }, // loading a network image or local resource using an URL.
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center
                        )
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(EXTRA_LARGE_PADDING)) {
                        Text(
                            "Price: ${product.productPrice}",
                            style = MaterialTheme.typography.titleMedium.copy(background = MaterialTheme.colorScheme.primaryContainer)
                        )
                        Text(
                            "Quantity: ${product.productQuantity}",
                            style = MaterialTheme.typography.titleMedium.copy(background = MaterialTheme.colorScheme.primaryContainer)
                        )
                    }
                }
            }
        }
        ListItem(
            modifier = Modifier.wrapContentSize(),
            headlineContent = { Text(product.productName) },
            supportingContent = { Text(product.productDescription) }
        )
        for (s in product.productSummarys ?: emptyList()) {
            Text(
                modifier = Modifier.drawUnderLine(MaterialTheme.colorScheme.primary, 0.5f),
                text = s,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

    @Composable
    private fun CartDisplay(boughtProduct: State<List<ProductModel>>) {
        Box {
            Icon(FeatherIcons.ShoppingCart, contentDescription = "amount of product bought")
            Text(
                boughtProduct.value.size.toString(),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.Green,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }


    @Composable
    private fun StoreItemDisplay(item: ProductModel, onClick: () -> Unit) {
        Column(
            Modifier
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.inversePrimary,
                    MaterialTheme.shapes.medium
                )
                .padding(SMALL_PADDING)
                .clickable { onClick() },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(Res.drawable.market),
                contentDescription = "store image"
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(EXTRA_LARGE_PADDING)
            ) {
                HorizontalTextIcon(
                    text = item.productPrice.toString(),
                    textStyle = MaterialTheme.typography.titleMedium,
                    leadingIcon = { Icon(FeatherIcons.Hash, contentDescription = "Price") }
                )

                HorizontalTextIcon(
                    text = item.productQuantity.toString(),
                    textStyle = MaterialTheme.typography.titleMedium,
                    leadingIcon = {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = "Quantity"
                        )
                    }
                )
            }
            Text(
                item.productName,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                item.productDescription,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }


    @Composable
    private fun BonanzaDetails(modifier: Modifier = Modifier) {
        Column(modifier, verticalArrangement = Arrangement.Center) {
            Text("Bonanza details")
        }
    }

    @Composable
    private fun StoreOwnerDetails(modifier: Modifier = Modifier, storeModel: StoreModel) {
        Row(modifier, verticalAlignment = Alignment.CenterVertically) {
            CoilImage(
//                modifier = Modifier.size(EXTRA_LARGE_PADDING * 1.5f),
                imageModel = { "https://picsum.photos/200" },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.FillBounds,
                )
            )
            if (storeModel.storeName.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight()
                        .createShimmer(listOf(Color.DarkGray, Color.LightGray)),
                )
            } else {
                ListItem(
                    modifier = Modifier.wrapContentSize().fillMaxHeight(),
                    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    headlineContent = { Text(text = storeModel.storeName) },
                    supportingContent = { Text(text = storeModel.storeDescription) },
                    overlineContent = { Text(text = "Number of product ${storeModel.products.size}") }
                )
            }
        }
    }
}