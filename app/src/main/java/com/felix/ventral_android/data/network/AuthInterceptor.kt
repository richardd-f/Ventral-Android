package com.felix.ventral_android.data.network

import javax.inject.Inject
import com.felix.ventral_android.data.local.LocalDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor @Inject constructor(
    private val localDataStore: LocalDataStore
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // 1. Get the token from DataStore (using runBlocking because intercept is synchronous)
        val token = runBlocking {
            localDataStore.getAuthToken().first()
        }

        val requestBuilder = chain.request().newBuilder()

        // 2. If token exists, add it to the Authorization header
        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}