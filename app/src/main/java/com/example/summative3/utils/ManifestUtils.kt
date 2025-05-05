package com.example.summative3.utils

import android.content.Context
import android.content.pm.PackageManager

object ManifestUtils {
    // Going to retrieve the api key from the application
    fun getApiFromManifest(context: Context):String?{
        return try {
            val applicationInfo = context.packageManager.
            getApplicationInfo(context.packageName,
                PackageManager.GET_META_DATA)
            val bundle = applicationInfo.metaData
            bundle.getString("com.google.android.geo.API_KEY")
        } catch(e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            null
        }
    }
}