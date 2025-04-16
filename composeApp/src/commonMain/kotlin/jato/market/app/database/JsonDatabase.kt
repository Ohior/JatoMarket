package jato.market.app.database

import jato.market.app.data_model.ProductModel
import jato.market.app.data_model.StoreModel
import jato.market.app.data_model.UserModel
import kotlinx.coroutines.flow.Flow

abstract class JsonDatabase {
    abstract fun getUserData():UserModel?
    abstract fun getStoreData():StoreModel?
    abstract fun getProductsData():List<ProductModel>?
    abstract fun getUserDataFlow(): Flow<UserModel>
}