package jato.market.app

import dev.jordond.compass.Coordinates
import dev.jordond.compass.Priority
import dev.jordond.compass.geocoder.MobileGeocoder
import dev.jordond.compass.geocoder.placeOrNull
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.LocationRequest
import dev.jordond.compass.geolocation.mobile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MobileJMapManager : JMapManager {
    private val geolocator = Geolocator.mobile()
    private val geocoder = MobileGeocoder()
    override val isTrackingActive: Flow<Boolean> = geolocator.trackingStatus.map { it.isActive }

    suspend fun initializeLocation(): Coordinates? {
        return when (val result = geolocator.current()) {
            is GeolocatorResult.Error -> return when (result) {
                is GeolocatorResult.PermissionDenied -> {
                    println("Location error : ${result.message}")
                    null
                }

                is GeolocatorResult.NotSupported -> {
                    println("Location error : ${result.message}")
                    null
                }

                is GeolocatorResult.NotFound -> {
                    println("Location error : ${result.message}")
                    null
                }

                is GeolocatorResult.GeolocationFailed -> {
                    println("Location error : ${result.message}")
                    null
                }

                else -> {
                    println("Location error : ${result.message}")
                    null
                }

            }

            is GeolocatorResult.Success -> {
                result.data.coordinates
            }
        }
    }

    override suspend fun startTracking() {
        geolocator.startTracking(LocationRequest(Priority.HighAccuracy))
    }

    override fun stopTracking() {
        geolocator.stopTracking()
    }

    override suspend fun getJLocation(): JLocation? {
        val location = initializeLocation() ?: return null
        val place = geocoder.placeOrNull(location) ?: return null
        return JLocation(
            lat = location.latitude,
            long = location.longitude,
            country = place.country,
            city = place.locality
        )
    }

    override fun trackJLocation(): Flow<JLocation>? {
        return geolocator.locationUpdates.map {
            val place = geocoder.placeOrNull(it.coordinates)
            JLocation(
                it.coordinates.latitude,
                it.coordinates.longitude,
                place?.country,
                place?.locality
            )
        }
    }
}
