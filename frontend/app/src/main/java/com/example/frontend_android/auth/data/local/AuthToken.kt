package com.example.frontend_android.auth.data.local

import android.content.Context

/**
 * gets the token from shared preferences
 * @author Ömer Aynaci
 * @param context access application specific classes and resorces
 * @return the shared preference
 */
fun getToken(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences("token", Context.MODE_PRIVATE)
    return sharedPreferences.getString("auth_token", null)
}