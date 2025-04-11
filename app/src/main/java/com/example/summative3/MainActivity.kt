package com.example.summative3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.summative3.data.EventDatabase
import com.example.summative3.ui.MainScreen
import com.example.summative3.ui.theme.Summative3Theme
import com.example.summative3.viewmodel.EventViewModel
import com.example.summative3.viewmodel.EventViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = EventDatabase.getDatabase(applicationContext)
        val viewModelFactory = EventViewModelFactory(database.eventDao())

        setContent {
            Summative3Theme {
                val eventViewModel: EventViewModel = viewModel(factory = viewModelFactory)
                MainScreen(viewModel = eventViewModel)
            }
        }
    }
}
