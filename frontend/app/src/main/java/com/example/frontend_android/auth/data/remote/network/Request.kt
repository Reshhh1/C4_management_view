package com.example.frontend_android.auth.data.remote.network

import com.example.frontend_android.util.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 *
 * adding the authorization header
 * @author Ömer Aynaci
 * @param token the jwt token
 * @return an instance of retrofit
 */
fun authorizationHeader(token: String?): Retrofit {
    val okHttpClient = OkHttpClient.Builder().apply {
        if (token != null) {
            addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(request)
            }
        }
    }
        .build()
    return retrofitInstance(okHttpClient)
}

/**
 * retrofit instance for network requests
 * @author Ömer Aynaci
 * @param okHttpClient okHttpClient instance
 * @param requestUrl base url
 * @return an instance of retrofit
 */
fun retrofitInstance(
    okHttpClient: OkHttpClient,
    requestUrl: String = Constants.BASE_URL
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(requestUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
}