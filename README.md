# Palmprint-Recognition-Frontend

Offline Integrated Recognition Platform based on Palmprint Recognition Module

### MVVM Pattern

data/ Model · Repository · Network 계층:
서버 통신, 로컬 저장소, 데이터 모델을 담당하는 계층입니다.
```
data/
 ├── api/                         # Retrofit API 인터페이스
 │    ├── AuthApi.kt              # 로그인/회원가입/탈퇴
 │    ├── UserApi.kt              # 사용자 Palmprint/History 관련 API
 │    └── AdminApi.kt             # 관리자 User/Device/Report 관리 API
 │
 ├── di/
 │    └── DataModule.kt           # Retrofit, Gson, Repository Hilt DI 제공
 │
 ├── local/
 │    ├── PreferenceManager.kt    # JWT 토큰/ROLE/로그인 상태 저장 (SharedPreferences)
 │    └── AuthLocalDataSource.kt  # (확장 가능) Local 인증 관리
 │
 ├── model/
 │    ├── AuthModels.kt           # 로그인/회원가입/토큰 모델
 │    ├── UserModels.kt           # 사용자 데이터 모델
 │    ├── AdminModels.kt          # 관리자 데이터 모델
 │    ├── ApiException.kt         # API 예외 포맷
 │    └── ErrorResponse.kt        # 서버 에러 모델
 │
 ├── network/
 │    └── NetworkConfig.kt        # BASE_URL, OkHttp 설정, Interceptor
 │
 └── repository/
      ├── AuthRepository.kt       # 로그인/회원가입/회원탈퇴/토큰관리
      ├── UserRepository.kt       # 유저 기능 관리
      └── AdminRepository.kt      # 관리자 기능 관리
                   # 카메라/갤러리/저장소 권한 처리


```

ui/ UI(View) · ViewModel 계층:
Jetpack Compose 기반 화면과 ViewModel 로직을 포함합니다.
```
ui/
 ├── auth/                          # 로그인/회원가입/역할 선택
 │    ├── AuthNavigation.kt         # Auth NavGraph
 │    ├── AuthRoutes.kt             # Auth 네비게이션 Route 정의
 │    ├── AuthViewModel.kt          # 로그인/회원가입/탈퇴 ViewModel
 │    ├── LoginScreen.kt            # 로그인 화면
 │    ├── SignUpScreen.kt           # 회원가입 화면
 │    └── RoleSelectionScreen.kt    # 사용자/관리자 선택
 │
 ├── navigation/
 │    └── AppNavHost.kt             # 앱 전체 NAVIGATION ROOT
 │
 ├── admin/
 │    ├── common/
 │    │    └── UiState.kt           # 로딩/성공/오류 상태 관리
 │    │
 │    ├── dashboard/
 │    │    └── AdminDashboardScreen.kt
 │    │
 │    ├── device_management/
 │    │    ├── DeviceListScreen.kt
 │    │    ├── DeviceDetailScreen.kt
 │    │    ├── AddDeviceScreen.kt
 │    │    ├── DeleteDeviceScreen.kt
 │    │    └── ViewModels...
 │    │
 │    ├── palmprint_management/
 │    │    ├── PalmprintListScreen.kt
 │    │    ├── UploadPalmprintScreen.kt
 │    │    ├── DeletePalmprintScreen.kt
 │    │    └── ViewModels...
 │    │
 │    ├── report_management/
 │    │    ├── ReportListScreen.kt
 │    │    ├── ReportDetailScreen.kt
 │    │    └── ViewModels...
 │    │
 │    ├── user_management/
 │    │    ├── UserListScreen.kt
 │    │    ├── UserDetailScreen.kt
 │    │    ├── AddUserScreen.kt
 │    │    ├── DeleteUserScreen.kt
 │    │    └── ViewModels...
 │    │
 │    ├── verification/
 │    │    ├── VerificationListScreen.kt
 │    │    └── ViewModels...
 │    │
 │    └── navigation/
 │         ├── AdminNavigation.kt
 │         ├── AdminAppRoot.kt
 │         └── AdminRoutes.kt
 │
 ├── user/
 │    ├── main/
 │    │    ├── MainScreen.kt
 │    │    ├── DeleteAccountScreen.kt
 │    │    └── MainViewModel.kt
 │    │
 │    └── navigation/
 │         ├── UserNavigation.kt
 │         └── UserRoutes.kt
 │
 └── theme/                           # 앱 디자인 시스템
      ├── Color.kt
      ├── Theme.kt
      └── Type.kt

```

### Example

```
[사용자(User)]
 ↓ (버튼 클릭: 일반 사용자 로그인)
[RoleSelectionScreen] ← Compose View
 ↓ (UI 이벤트 → ViewModel 메서드 호출)
[AuthViewModel.onLoginClick()]
 ↓ (비즈니스 로직 → Repository 호출)
[AuthRepository.login()]
 ↓ (Retrofit API 통신)
[AuthApi.login()]
 ↓ (서버로 HTTP 요청)
[Backend API Server]
```

