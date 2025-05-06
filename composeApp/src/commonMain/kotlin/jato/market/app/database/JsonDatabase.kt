package jato.market.app.database

import jato.market.app.data_model.ProductModel
import jato.market.app.data_model.StoreModel
import jato.market.app.data_model.UserModel
import kotlinx.coroutines.flow.Flow

interface JsonDatabase {
    suspend fun getUserData(): UserModel?
    suspend fun getStoreData(): StoreModel?
    suspend fun getProductsData(): List<ProductModel>?
    fun getUserDataFlow(): Flow<UserModel>
}
