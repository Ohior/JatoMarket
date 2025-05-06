package jato.market.app.screens.store

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import jato.app.jato_utils.ifNotNull
import jato.market.app.data_model.ProductModel
import jato.market.app.data_model.StoreModel
import jato.market.app.data_model.UserModel
import jato.market.app.database.remote.JatoDatabase
import kotlinx.coroutines.launch


sealed class StoreScreenType {
    data class Product(val product: ProductModel) : StoreScreenType()
    data object Products : StoreScreenType()
}

class StoreScreenModel(userDocumentId: String, storeDocumentId: String) : ScreenModel {
    private val _boughtProduct = mutableStateOf<List<ProductModel>>(emptyList())
    val boughtProduct: State<List<ProductModel>> = _boughtProduct
    var user by mutableStateOf<UserModel>(UserModel.empty())
        private set
    var store by mutableStateOf<StoreModel>(StoreModel.empty())
        private set
    var storeScreenType by mutableStateOf<StoreScreenType>(StoreScreenType.Products)
    init {
        screenModelScope.launch {
            JatoDatabase.getUserData(userDocumentId).ifNotNull {
                user = it
            }
            JatoDatabase.getStoreData(storeDocumentId).ifNotNull {
                store = it
            }
        }
    }
}