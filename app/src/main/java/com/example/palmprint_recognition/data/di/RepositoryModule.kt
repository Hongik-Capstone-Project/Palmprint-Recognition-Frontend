package com.example.palmprint_recognition.data.di

import com.example.palmprint_recognition.data.repository.AdminRepository
import com.example.palmprint_recognition.data.repository.AdminRepositoryImpl
import com.example.palmprint_recognition.data.repository.AuthRepository
import com.example.palmprint_recognition.data.repository.AuthRepositoryImpl
import com.example.palmprint_recognition.data.repository.DemoRepository
import com.example.palmprint_recognition.data.repository.DemoRepositoryImpl
import com.example.palmprint_recognition.data.repository.UserRepository
import com.example.palmprint_recognition.data.repository.UserRepositoryImpl
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
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindAdminRepository(impl: AdminRepositoryImpl): AdminRepository

    @Binds
    @Singleton
    abstract fun bindDemoRepository(impl: DemoRepositoryImpl): DemoRepository
}