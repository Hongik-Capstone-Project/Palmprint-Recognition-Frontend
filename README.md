# Palmprint-Recognition-Frontend

Offline Integrated Recognition Platform based on Palmprint Recognition Module

### MVVM Pattern

```
app/
 ├── data/                                       # 🧩 Model (데이터/서버 계층)
 │   ├── api/                                    # Retrofit 인터페이스 (서버 명세 기반)
 │   │    ├── AuthApi.kt                         # ✅ 로그인/로그아웃 (유저/관리자 공용)
 │   │    ├── UserApi.kt                         # 유저 UC-1~17 (회원가입~인증 알림)
 │   │    └── AdminApi.kt                        # ✅ 관리자 UC-22~28 (유저/디바이스/통계 관리)
 │   │
 │   ├── model/                                  # 요청·응답 데이터 클래스
 │   │    ├── AuthModels.kt                      # ✅ 로그인, 토큰, 세션 등
 │   │    ├── UserModels.kt                      # 유저 관련 DTO (Palm, Payment, Institution 등)
 │   │    └── AdminModels.kt                     # ✅ 관리자 관련 DTO (User, Device, Stats 등)
 │   │
 │   ├── repository/                             # Repository: 데이터 접근 / Retrofit 호출 로직
 │   │    ├── AuthRepository.kt                  # ✅ 공용 로그인/로그아웃
 │   │    ├── UserRepository.kt                  # 유저 전용 기능 (UC-1~17)
 │   │    └── AdminRepository.kt                 # ✅ 관리자 전용 기능 (UC-22~28)
 │   │
 │   └── network/                                # 네트워크 설정
 │        ├── RetrofitInstance.kt                # Retrofit + BASE_URL + Gson 설정
 │        └── NetworkConfig.kt                   # (선택) Interceptor, Header, Token 설정
 │
 ├── ui/                                         # 🎨 View + ViewModel 계층
 │   ├── common/                                 # 공용 화면 (Splash, 로그인 선택 등)
 │   │    ├── SplashActivity.kt
 │   │    ├── RoleSelectionActivity.kt           # ✅ 관리자/유저 로그인 분기
 │   │    ├── AuthViewModel.kt                   # ✅ 공용 로그인 ViewModel
 │   │    └── components/                        # ✅ 공용 UI 컴포넌트
 │   │         ├── LoadingDialog.kt
 │   │         └── CustomButton.kt
 │   │
 │   ├── user/                                   # 유저용 UI + ViewModel
 │   │    ├── UserLoginActivity.kt
 │   │    ├── UserMainActivity.kt
 │   │    ├── UserViewModel.kt
 │   │    └── modules/                           # (선택) 세부 기능별 UI
 │   │         ├── PalmRegisterActivity.kt
 │   │         ├── PaymentListActivity.kt
 │   │         ├── InstitutionListActivity.kt
 │   │         └── HistoryListActivity.kt
 │   │
 │   └── admin/                                  # ✅ 관리자용 UI + ViewModel
 │        ├── AdminLoginActivity.kt
 │        ├── AdminMainActivity.kt
 │        ├── AdminViewModel.kt
 │        └── modules/                           # 관리자 세부 기능
 │             ├── UserManageActivity.kt
 │             ├── PalmManageActivity.kt
 │             ├── DeviceManageActivity.kt
 │             └── StatsDashboardActivity.kt
 │
 └── utils/                                      # ⚙️ 공통 유틸리티
      ├── Constants.kt                           # BASE_URL, Intent Keys, 공통 상수
      ├── PreferenceManager.kt                   # SharedPreferences (토큰/세션)
      ├── Extensions.kt                          # 공용 확장 함수 (Toast, View, Log 등)
      └── PermissionUtils.kt                     # 카메라/갤러리/저장소 권한 처리


```

### Example

```
[사용자(관리자)]
 ↓ (버튼 클릭 이벤트)
[AdminActivity] ← View
↓ (UI 이벤트 → ViewModel 메서드 호출)
[AdminViewModel]
↓ (비즈니스 로직 → Repository 호출)
[AdminRepository]
↓ (Retrofit API 통신)
[AdminApi]
↓ (서버로 HTTP 요청)
[Backend API Server]
```

### Convention
