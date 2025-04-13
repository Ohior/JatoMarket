package jato.market.app.screens.store

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
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
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import compose.icons.feathericons.Hash
import compose.icons.feathericons.ShoppingCart
import jato.app.jato_utils.JDevice
import jato.app.jato_utils.rememberJDevice
import jato.market.app.data_model.ComposeWidget
import jato.market.app.data_model.ProductModel
import jato.market.app.theme.AutoSwitcher
import jato.market.app.theme.EXTRA_LARGE_PADDING
import jato.market.app.theme.HorizontalTextIcon
import jato.market.app.theme.SMALL_PADDING
import jatomarket_.composeapp.generated.resources.Res
import jatomarket_.composeapp.generated.resources.market
import org.jetbrains.compose.resources.painterResource


private val items = List(100) {
    ProductModel(
        productName = "Your current implementation",
        productDescription = "Yes, your approach will work in a ViewModel, but there are some improvements you can make to follow best practices. Here's how to properly handle state in a ViewModel",
        productPrice = 20.5,
        productQuantity = 52,
        storeId = "Data",
        )
}


class StoreScreen(private val storeId: String, private val userId: String) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = rememberScreenModel { StoreScreenModel() }
        val device = rememberJDevice()
        Scaffold(
            Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text("Store $storeId $userId") },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceBright),
                    actions = {
                        CartDisplay(
                            screenModel.boughtProduct
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            if (screenModel.storeScreenType !is StoreScreenType.Products){
                                screenModel.storeScreenType = StoreScreenType.Products
                            }else {
                                navigator.pop()
                            }                        }) {
                            Icon(FeatherIcons.ArrowLeft, "Go Back")
                        }
                    }
                )
            }
        ) { pv ->
            Column(Modifier.padding(pv).fillMaxSize()) {
                when (device) {
                    is JDevice.Portrait -> {
                        AutoSwitcher(
                            listOf(
                                ComposeWidget { StoreOwnerDetails(Modifier.weight(1f)) },
                                ComposeWidget { BonanzaDetails(Modifier.weight(1f)) }
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
                                Modifier
                                    .size(halfWidth.dp, EXTRA_LARGE_PADDING * 2)
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
                    StoreScreenType.Owner -> Unit

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
                            items(items) { item ->
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
        // TODO: Function DisplayProduct in StoreScreen not implemented
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
    private fun BonanzaDetails(modifier: Modifier) {
        Column(modifier, verticalArrangement = Arrangement.Center) {
            Text("Bonanza details")
        }
    }

    @Composable
    private fun StoreOwnerDetails(modifier: Modifier) {
        Row(modifier, verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier.size(EXTRA_LARGE_PADDING * 1.5f),
                painter = painterResource(Res.drawable.market),
                contentScale = ContentScale.FillBounds,
                contentDescription = "Owner profile image"
            )
            ListItem(
                modifier = Modifier.wrapContentSize(),
                colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                headlineContent = { Text("Store name") },
                supportingContent = { Text("Store owner details") },
                overlineContent = { Text("owner name") }
            )
        }
    }
}