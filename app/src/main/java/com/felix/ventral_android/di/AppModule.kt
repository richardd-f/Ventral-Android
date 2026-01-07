package com.felix.ventral_android.di

import android.content.Context
import com.felix.ventral_android.data.local.LocalDataStore
import com.felix.ventral_android.data.network.AuthInterceptor
import com.felix.ventral_android.data.network.api.EventApiService
import com.felix.ventral_android.data.network.api.UserApiService
import com.felix.ventral_android.data.network.cloudinary.CloudinaryManager
import com.felix.ventral_android.data.repository.EventRepositoryImpl
import com.felix.ventral_android.data.repository.UserRepositoryImpl
import com.felix.ventral_android.domain.repository.EventRepository
import com.felix.ventral_android.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Provide OkHttpClient
    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor) // Attach the token injector here
            .build()
    }

    // Provide Retrofit (The base network client)
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://ventral.felitech.site/api/")
            .client(okHttpClient) // Attach the token injector here
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Provide LocalDataStore
    @Provides
    @Singleton
    fun provideLocalDataSource(@ApplicationContext context: Context): LocalDataStore {
        return LocalDataStore(context)
    }

    // Provide Cloudinary manager
    @Provides
    @Singleton
    fun provideCloudinaryManager(
        @ApplicationContext context: Context
    ): CloudinaryManager {
        return CloudinaryManager(context)
    }

    // Provide UserApiService
    @Provides
    @Singleton
    fun provideUserApiService(retrofit: Retrofit): UserApiService {
        return retrofit.create(UserApiService::class.java)
    }

    // Provide User Repository
    @Provides
    @Singleton
    fun provideUserRepository(
        apiService: UserApiService,
        localDataStore: LocalDataStore,
        cloudinaryManager: CloudinaryManager
    ): UserRepository {
        return UserRepositoryImpl( apiService, localDataStore, cloudinaryManager )
    }

    // Provide Event Api Service
    @Provides
    @Singleton
    fun provideEventApiService(retrofit: Retrofit): EventApiService {
        return retrofit.create(EventApiService::class.java)
    }

    // Provide Event Repository
    @Provides
    @Singleton
    fun provideEventRepository(
        apiService: EventApiService,
        localDataStore: LocalDataStore,
        cloudinaryManager: CloudinaryManager
    ): EventRepository {
        return EventRepositoryImpl(apiService, localDataStore, cloudinaryManager)
    }

}