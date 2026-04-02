package com.example.palmprint_recognition.data.di

import android.content.Context
import com.example.palmprint_recognition.data.api.AdminApi
import com.example.palmprint_recognition.data.api.AuthApi
import com.example.palmprint_recognition.data.api.DemoApi
import com.example.palmprint_recognition.data.api.UserApi
import com.example.palmprint_recognition.data.local.PreferenceManager
import com.example.palmprint_recognition.data.network.AuthInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val MAIN_BASE_URL = "http://43.203.103.107:8000/"
    private const val DEMO_BASE_URL = "http://13.125.203.213:8000/"

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun providePreferenceManager(
        @ApplicationContext context: Context
    ): PreferenceManager = PreferenceManager(context)

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        prefs: PreferenceManager
    ): AuthInterceptor = AuthInterceptor(prefs)

    @Provides
    @Singleton
    @Named("authClient")
    fun provideAuthOkHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @Named("demoClient")
    fun provideDemoOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    @Named("authRetrofit")
    fun provideAuthRetrofit(
        gson: Gson,
        @Named("authClient") okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(MAIN_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    @Named("demoRetrofit")
    fun provideDemoRetrofit(
        gson: Gson,
        @Named("demoClient") okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(DEMO_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(
        @Named("authRetrofit") retrofit: Retrofit
    ): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideUserApi(
        @Named("authRetrofit") retrofit: Retrofit
    ): UserApi = retrofit.create(UserApi::class.java)

    @Provides
    @Singleton
    fun provideAdminApi(
        @Named("authRetrofit") retrofit: Retrofit
    ): AdminApi = retrofit.create(AdminApi::class.java)

    @Provides
    @Singleton
    fun provideDemoApi(
        @Named("demoRetrofit") retrofit: Retrofit
    ): DemoApi = retrofit.create(DemoApi::class.java)
}