package com.felix.ventral_android.data.repository

import com.felix.ventral_android.data.dto.ApiResponse
import com.felix.ventral_android.data.local.LocalDataStore
import com.google.gson.Gson

abstract class BaseRepository {
    abstract val localDataStore: LocalDataStore
    protected suspend fun <T, R> handleApiCall(
        call: suspend () -> retrofit2.Response<ApiResponse<T>>,
        map: suspend (T) -> R
    ): Result<R> {
        return try {
            val response = call()
            val body = response.body()

            if (response.isSuccessful && body != null && body.success && body.data != null) {
                Result.success(map(body.data))
            } else if (response.code() == 401) {
                // 1. The token is expired or invalid
                // 2. We should trigger a logout event here
                localDataStore.clearData()
                Result.failure(Exception("Session expired. Please login again."))
            } else {
                val errorBodyString = response.errorBody()?.string()

                // Explicitly cast to avoid the ambiguity error
                val errorResponse = try {
                    Gson().fromJson(errorBodyString, ApiResponse::class.java)
                } catch (e: Exception) {
                    null
                }

                val errorMessage = errorResponse?.errors ?: errorResponse?.message ?: "Unknown Error"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}