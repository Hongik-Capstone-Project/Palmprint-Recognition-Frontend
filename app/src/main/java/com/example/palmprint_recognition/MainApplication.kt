package com.example.palmprint_recognition

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Hilt를 사용하기 위한 Application 클래스입니다.
 * @HiltAndroidApp 어노테이션은 Hilt의 코드 생성을 트리거하고,
 * 앱의 의존성 주입을 위한 기반을 마련합니다.
 */
@HiltAndroidApp
class MainApplication : Application()