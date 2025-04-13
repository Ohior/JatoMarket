package jato.market.app.screens.store

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import jato.market.app.data_model.ProductModel


sealed class StoreScreenType {
    data class Product(val product: ProductModel):StoreScreenType()
    data object Products:StoreScreenType()
    data object Owner:StoreScreenType()
}

class StoreScreenModel : ScreenModel {
    private val _boughtProduct = mutableStateOf<List<ProductModel>>(emptyList())
    val boughtProduct: State<List<ProductModel>> = _boughtProduct

    var storeScreenType by mutableStateOf<StoreScreenType>(StoreScreenType.Products)

    fun addProduct(product: ProductModel) {
        _boughtProduct.value += product
    }
}