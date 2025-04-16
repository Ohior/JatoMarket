package jato.market.app.database.remote

import jato.market.app.data_model.ProductModel
import jato.market.app.data_model.StoreModel
import jato.market.app.data_model.UserModel
import jato.market.app.database.JsonDatabase
import kotlinx.coroutines.flow.Flow

class JsonRemoteDatabase : JsonDatabase() {
    override fun getUserData(): UserModel? {
        // get user model from url
        // get store from url using the store id you got from user model
        // update the store model in the user model
        // return the user model
        throw NotImplementedError("This function 'getUserData in JsonRemoteDatabase' is not implemented")
    }

    override fun getStoreData(): StoreModel? {
        val user = getUserData()
        return user?.store
    }

    override fun getProductsData(): List<ProductModel>? {
        val user = getUserData()
        return user?.store?.products
    }

    override fun getUserDataFlow(): Flow<UserModel> {
        // get user model from url
        // get store from url using the store id you got from user model
        // update the store model in the user model
        // return the user model as flow
        throw NotImplementedError("This function 'getUserDataFlow in JsonRemoteDatabase' is not implemented")
    }

    fun getAllStoresData(): List<StoreModel> {
        // get all stores from url
        // return the list of stores
        throw NotImplementedError("This function 'getAllStoresData in JsonRemoteDatabase' is not implemented")
    }
}