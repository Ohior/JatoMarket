package jato.market.app.database

import jato.market.app.data_model.ProductModel
import jato.market.app.data_model.StoreModel
import jato.market.app.data_model.UserModel
import kotlinx.coroutines.flow.Flow

interface JsonDatabase {
    fun getUserData():UserModel?
    fun getStoreData():StoreModel?
    fun getProductsData():List<ProductModel>?
    fun getUserDataFlow(): Flow<UserModel>
}