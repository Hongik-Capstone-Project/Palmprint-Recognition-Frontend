package com.example.palmprint_recognition.data.di

import com.example.palmprint_recognition.data.repository.DemoRepository
import com.example.palmprint_recognition.data.repository.DemoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDemoRepository(impl: DemoRepositoryImpl): DemoRepository
}