package jato.market.app

import kotlinx.coroutines.flow.Flow


data class JLocation(val lat: Double?, val long: Double?, val country: String? = null, val city: String? = null )


interface JMapManager {
    val isTrackingActive: Flow<Boolean>
    suspend fun startTracking()
    fun stopTracking()
    suspend fun getJLocation(): JLocation?
    fun trackJLocation():Flow<JLocation>?
}
