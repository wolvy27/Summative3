package com.example.summative3

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.summative3.data.EventDatabase
import com.example.summative3.ui.MainScreen
import com.example.summative3.ui.theme.Summative3Theme
import com.example.summative3.utils.ManifestUtils
import com.example.summative3.viewmodel.EventViewModel
import com.example.summative3.viewmodel.EventViewModelFactory
import com.example.summative3.viewmodel.MapViewModel
import com.example.summative3.viewmodel.MapViewModelFactory
import com.google.android.libraries.places.api.Places

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(
                this,
                "Notification permission denied - some features may not work",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mapsApiKey = ManifestUtils.getApiFromManifest(this)
        if (mapsApiKey.isNullOrEmpty()) {
            Toast.makeText(this, "Google Maps API key missing in manifest!", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        try {
            if (!Places.isInitialized()) {
                Places.initialize(applicationContext, mapsApiKey)
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to initialize Maps services", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                }
                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    Toast.makeText(
                        this,
                        "Please allow notifications to get alerts about your events",
                        Toast.LENGTH_LONG
                    ).show()
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }

        val database = EventDatabase.getDatabase(applicationContext)
        val eventDao = database.eventDao()

        setContent {
            Summative3Theme {
                val eventViewModel: EventViewModel = viewModel(
                    factory = EventViewModelFactory(eventDao, applicationContext)
                )
                val mapViewModel: MapViewModel = viewModel(
                    factory = MapViewModelFactory(eventDao)
                )
                MainScreen(
                    eventViewModel = eventViewModel,
                    mapViewModel = mapViewModel
                )
            }
        }
    }
}