package com.example.summative3.viewmodel

import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build // Import Build for version checks if needed
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.summative3.data.Event // Ensure correct import
import com.example.summative3.data.EventDao // Ensure correct import
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber // Ensure Timber is imported
import java.io.IOException // Import specific exception

class MapViewModel(
    private val eventDao: EventDao
) : ViewModel() {

    private val _userLocation = mutableStateOf<LatLng?>(null)
    val userLoc: State<LatLng?> = _userLocation

    private val _selectedLocation = mutableStateOf<LatLng?>(null)
    val selLoc: State<LatLng?> = _selectedLocation

    private val _eventMarkers = mutableStateOf<List<Pair<Event, LatLng>>>(emptyList())
    val eventMarkers: State<List<Pair<Event, LatLng>>> = _eventMarkers

    fun fetchUserLocation(context: Context, fusedLocationProviderClient: FusedLocationProviderClient) {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        _userLocation.value = LatLng(it.latitude, it.longitude)
                        Timber.d("User location fetched: ${_userLocation.value}")
                    } ?: Timber.w("Last location was null.")
                }.addOnFailureListener { e ->
                    Timber.e(e, "Failed to get last location.")
                }
            } catch (e: SecurityException) {
                Timber.e(e, "SecurityException: Permission for location access was revoked or missing.")
            }
        } else {
            Timber.e("Location permission is not granted. Cannot fetch user location.")
        }
    }

    fun selectLocation(selectedPlace: String, context: Context) {
        viewModelScope.launch {
            if (!Geocoder.isPresent()) {
                Timber.e("Geocoder service not available.")
                return@launch
            }
            val geocoder = Geocoder(context)
            try {
                val addresses = withContext(Dispatchers.IO) {
                    @Suppress("DEPRECATION")
                    geocoder.getFromLocationName(selectedPlace, 1) // Max 1 result
                }

                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    _selectedLocation.value = LatLng(address.latitude, address.longitude)
                    Timber.d("Selected location '$selectedPlace' geocoded to: ${_selectedLocation.value}")
                } else {
                    Timber.e("No location found for search query: $selectedPlace")
                }
            } catch (e: IOException) {
                Timber.e(e, "Geocoding failed for '$selectedPlace' due to network/backend issue.")
            } catch (e: IllegalArgumentException) {
                Timber.e(e, "Geocoding failed for '$selectedPlace' due to invalid input.")
            } catch (e: Exception) {
                Timber.e(e, "An unexpected error occurred during geocoding for '$selectedPlace'.")
            }
        }
    }

    fun fetchAllEventMarkers(context: Context) {
        viewModelScope.launch {
            val events = withContext(Dispatchers.IO) {
                try {
                    eventDao.getAllEvents().first()
                } catch (e: Exception) {
                    Timber.e(e, "Error fetching events from database")
                    emptyList<Event>()
                }
            }

            if (events.isEmpty() || !Geocoder.isPresent()) {
                if (!Geocoder.isPresent()) Timber.e("Geocoder not available, cannot fetch markers.")
                _eventMarkers.value = emptyList()
                return@launch
            }

            val geocoder = Geocoder(context)
            val markers = mutableListOf<Pair<Event, LatLng>>()

            withContext(Dispatchers.IO) {
                events.forEach { event ->
                    if (event.location.isNotBlank()) {
                        try {
                            @Suppress("DEPRECATION")
                            val addresses = geocoder.getFromLocationName(event.location, 1)

                            addresses?.firstOrNull()?.let { address ->
                                markers.add(event to LatLng(address.latitude, address.longitude))
                            } ?: run {
                                Timber.w("Geocoding returned no address for event '${event.name}' location: ${event.location}")
                            }
                        } catch (e: IOException) {
                            Timber.e(e, "Geocoding failed for event '${event.name}' location '${event.location}' due to network/backend issue.")
                        } catch (e: IllegalArgumentException) {
                            Timber.e(e, "Geocoding failed for event '${event.name}' location '${event.location}' due to invalid input.")
                        } catch (e: Exception) {
                            Timber.e(e, "Unexpected error geocoding event '${event.name}' location '${event.location}'.")
                        }
                    } else {
                        Timber.w("Event ID ${event.id} ('${event.name}') has a blank location, skipping marker.")
                    }
                }
            }
            _eventMarkers.value = markers
            Timber.d("Fetched ${markers.size} event markers.")
        }
    }
}