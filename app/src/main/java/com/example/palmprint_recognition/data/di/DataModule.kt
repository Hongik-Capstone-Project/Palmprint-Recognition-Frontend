package com.example.palmprint_recognition.data.di

import com.example.palmprint_recognition.data.api.AdminApi
import com.example.palmprint_recognition.data.api.AuthApi
import com.example.palmprint_recognition.data.api.UserApi
import com.example.palmprint_recognition.data.repository.AdminRepository
import com.example.palmprint_recognition.data.repository.AuthRepository
import com.example.palmprint_recognition.data.repository.UserRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideAdminApi(retrofit: Retrofit): AdminApi = retrofit.create(AdminApi::class.java)

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)

    @Provides
    @Singleton
    fun provideAuthRepository(authApi: AuthApi, gson: Gson): AuthRepository = AuthRepository(authApi, gson)

    @Provides
    @Singleton
    fun provideAdminRepository(adminApi: AdminApi, gson: Gson): AdminRepository = AdminRepository(adminApi, gson)

    @Provides
    @Singleton
    fun provideUserRepository(userApi: UserApi, gson: Gson): UserRepository = UserRepository(userApi, gson)
}