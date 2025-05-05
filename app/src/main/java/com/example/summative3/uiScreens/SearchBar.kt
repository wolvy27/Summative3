package com.example.summative3.uiScreens

import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.widget.addTextChangedListener
import com.example.summative3.utils.ManifestUtils
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onPlaceSelected: (String) -> Unit
)
{
    val textColor = if(isSystemInDarkTheme()) Color.White else Color.Black

    AndroidView(
        factory = { context ->
            AutoCompleteTextView(context).apply {
                hint = "Search for a place"
                setTextColor(textColor.toArgb())
                setHintTextColor(textColor.copy(alpha = 0.6f).toArgb())
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                val autoCompleteAdapter = ArrayAdapter<String>(context,
                    android.R.layout.simple_dropdown_item_1line)
                val placesClient = Places.createClient(context)
                val autocompleteSessionToken = AutocompleteSessionToken.newInstance()

                addTextChangedListener{ editable ->
                    val query = editable?.toString() ?: ""
                    if (query.isNotEmpty()) {
                        val request = FindAutocompletePredictionsRequest.builder()
                            .setSessionToken(autocompleteSessionToken)
                            .setQuery(query)
                            .build()
                        placesClient.findAutocompletePredictions(request)
                            .addOnSuccessListener { response ->
                                autoCompleteAdapter.clear()
                                response.autocompletePredictions.forEach { prediction ->
                                    autoCompleteAdapter.add(prediction.
                                    getFullText(null).toString())

                                }
                                autoCompleteAdapter.notifyDataSetChanged()

                            }
                    }
                }
                setAdapter(autoCompleteAdapter)
                setOnItemClickListener{_,_,position,_ ->
                    val selectedPlace = autoCompleteAdapter.getItem(position) ?:
                    return@setOnItemClickListener
                    onPlaceSelected(selectedPlace)

                }
            }

        },
        modifier = modifier.fillMaxWidth().padding(16.dp)
    )
}