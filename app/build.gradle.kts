plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    namespace = "com.example.palmprint_recognition"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.palmprint_recognition"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures { compose = true }
}

dependencies {

    // -----------------------------
    // 1) Compose BOM — 버전 통합
    // -----------------------------
    val composeBom = platform("androidx.compose:compose-bom:2024.10.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // -----------------------------
    // 2) Compose core libs
    // -----------------------------
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")


    // Pull-to-refresh API 포함
    implementation("androidx.compose.foundation:foundation")

    // Optional adaptive layout
    implementation("androidx.compose.material3.adaptive:adaptive")

    // Preview tools
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // -----------------------------
    // AndroidX / Activity / Lifecycle
    // -----------------------------
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.5")
    implementation("androidx.activity:activity-compose:1.12.0")

    // Navigation Compose
    implementation(libs.androidx.navigation.compose)

    // -----------------------------
    // Hilt
    // -----------------------------
    implementation("com.google.dagger:hilt-android:2.50")
    kapt("com.google.dagger:hilt-android-compiler:2.50")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // -----------------------------
    // Network
    // -----------------------------
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)
    implementation(libs.gson)

    // -----------------------------
    // Tests
    // -----------------------------
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
}
