package com.felix.ventral_android.di

import android.content.Context
import com.felix.ventral_android.data.local.LocalDataStore
import com.felix.ventral_android.data.network.api.UserApiService
import com.felix.ventral_android.data.network.cloudinary.CloudinaryManager
import com.felix.ventral_android.data.repository.UserRepositoryImpl
import com.felix.ventral_android.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Provide Retrofit (The base network client)
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.121.65.83:3000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Provide UserApiService
    @Provides
    @Singleton
    fun provideUserApiService(retrofit: Retrofit): UserApiService {
        return retrofit.create(UserApiService::class.java)
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

    // Provide User Repository
    @Provides
    @Singleton
    fun provideUserRepository(
        apiService: UserApiService,
        localDataSource: LocalDataStore,
        cloudinaryManager: CloudinaryManager
    ): UserRepository {
        return UserRepositoryImpl( apiService, localDataSource, cloudinaryManager )
    }
}