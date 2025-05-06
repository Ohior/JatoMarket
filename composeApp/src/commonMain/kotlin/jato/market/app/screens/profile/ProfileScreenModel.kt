package jato.market.app.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import jato.app.jato_utils.JUtils
import jato.app.jato_utils.getIfNotNull
import jato.market.app.data_model.StoreModel
import jato.market.app.data_model.UserModel
import jato.market.app.database.remote.JatoDatabase
import kotlinx.coroutines.launch

class ProfileScreenModel(userDocumentId: String) : ScreenModel {
    var user by mutableStateOf<UserModel?>(null)
        private set
    var readOnly by mutableStateOf(true)
    var createStorePopup by mutableStateOf(false)
    var firstName by mutableStateOf(user.getIfNotNull("") { it.firstName })
    var lastName by mutableStateOf(user.getIfNotNull("") { it.lastName })
    var email by mutableStateOf(user.getIfNotNull("") { it.email })
    var imageUrl by mutableStateOf(user?.imageUrl)
    var userLong by mutableStateOf(user.getIfNotNull(0.0) { it.longitude })
    var userLat by mutableStateOf(user.getIfNotNull(0.0) { it.latitude })

    val store = user?.store
    var storeName by mutableStateOf(store.getIfNotNull("") { it.storeName })
    var storeDescription by mutableStateOf(store.getIfNotNull("") { it.storeDescription })
    var storeLong by mutableStateOf(store?.longitude)
    var storeLat by mutableStateOf(store?.latitude)
    var storeImageUrl by mutableStateOf(store.getIfNotNull("") { it.imageUrl })
    val products = store?.products

    init {
        screenModelScope.launch {
            user = JatoDatabase.getUserData(userDocumentId)
            firstName = user.getIfNotNull("") { it.firstName }
            lastName = user.getIfNotNull("") { it.lastName }
            email = user.getIfNotNull("") { it.email }
            imageUrl = user?.imageUrl
        }
    }



    fun createUpdateStore() {
        screenModelScope.launch {
            JatoDatabase.updateStoreData(
                StoreModel(
                    storeName = storeName,
                    storeDescription = storeDescription,
                    userDocumentId = user?.documentId ?: "",
                    documentId = "$storeName ${JUtils.generateUniqueId()}",
                )
            )
        }
    }

    fun updateLongLat() {
    }
}