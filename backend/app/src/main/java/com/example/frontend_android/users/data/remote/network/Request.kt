package com.example.frontend_android.users.data.remote.network

import com.example.frontend_android.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * retrofit instance for network requests
 * @author Ömer Aynaci
 * @return an instance of retrofit
 */
fun retrofitInstance(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}