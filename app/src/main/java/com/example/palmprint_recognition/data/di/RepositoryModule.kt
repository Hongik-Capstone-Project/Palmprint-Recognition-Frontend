package com.example.palmprint_recognition.data.di

import com.example.palmprint_recognition.data.repository.AdminRepository
import com.example.palmprint_recognition.data.repository.AdminRepositoryImpl
import com.example.palmprint_recognition.data.repository.AuthRepository
import com.example.palmprint_recognition.data.repository.AuthRepositoryImpl
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

    // --- 실제 Repository 바인딩 (활성화) ---
    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindAdminRepository(impl: AdminRepositoryImpl): AdminRepository

    // --- 테스트를 위한 Fake Repository 바인딩 (비활성화) ---
    // @Binds
    // @Singleton
    // abstract fun bindFakeAuthRepository(impl: FakeAdminRepository): AuthRepository
    //
    // @Binds
    // @Singleton
    // abstract fun bindFakeAdminRepository(impl: FakeAdminRepository): AdminRepository
}