package com.example.palmprint_recognition.data.di

import com.example.palmprint_recognition.data.api.DemoApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // 데모 서버 주소
    private const val BASE_URL = "http://13.125.203.213:8000/"

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    /**
     * 데모 앱은 인증 토큰 없이 호출하므로
     * interceptor 없는 OkHttpClient 사용
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        gson: Gson,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideDemoApi(retrofit: Retrofit): DemoApi =
        retrofit.create(DemoApi::class.java)
}