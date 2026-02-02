package com.example.palmprint_recognition.data.di

import android.content.Context
import com.example.palmprint_recognition.data.api.AdminApi
import com.example.palmprint_recognition.data.api.AuthApi
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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // 실제 배포된 백엔드 서버 주소로 변경
    private const val BASE_URL = "http://43.203.103.107:8000/"

    // Gson
    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    // PreferenceManager (SharedPreferences 기반)
    @Provides
    @Singleton
    fun providePreferenceManager(
        @ApplicationContext context: Context
    ): PreferenceManager = PreferenceManager(context)

    // AuthInterceptor
    @Provides
    @Singleton
    fun provideAuthInterceptor(
        prefs: PreferenceManager
    ): AuthInterceptor = AuthInterceptor(prefs)

    // OkHttpClient (Interceptor 적용)
    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    // Retrofit (OkHttpClient 적용)
    @Provides
    @Singleton
    fun provideRetrofit(
        gson: Gson,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // 인터셉터 적용된 client 사용
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    // APIs
    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideAdminApi(retrofit: Retrofit): AdminApi =
        retrofit.create(AdminApi::class.java)

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi =
        retrofit.create(UserApi::class.java)
}
