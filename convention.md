# 🧩 Palmprint-Recognition-Frontend 코딩 컨벤션

> Android (Kotlin, MVVM) 기반 프로젝트의 일관된 코드 스타일 가이드라인  
> 팀 전체가 같은 네이밍, 파일 구조, 커밋 규칙을 유지하기 위한 문서입니다.

## 🔠 네이밍 규칙

| 항목            | 규칙                             | 예시                                     |
| --------------- | -------------------------------- | ---------------------------------------- |
| 패키지명        | 모두 소문자, snake_case          | `ui.admin.modules`                       |
| 클래스명        | PascalCase (대문자로 시작)       | `AdminViewModel`, `UserRepository`       |
| 인터페이스명    | PascalCase                       | `AdminApi`, `AuthListener`               |
| 데이터 클래스명 | PascalCase + 의미 명확히         | `UserResponse`, `CreateDeviceRequest`    |
| XML 파일명      | 소문자 + snake_case              | `activity_admin_main.xml`                |
| 함수명          | 소문자 + 동사로 시작 (camelCase) | `getAllUsers()`, `deleteUser()`          |
| 변수명          | camelCase                        | `userList`, `deviceId`, `authToken`      |
| Boolean 변수명  | `is`, `has`, `can` 접두사        | `isLoading`, `hasPermission`, `canRetry` |
| 상수명          | 대문자 + 언더바                  | `BASE_URL`, `TIMEOUT_LIMIT`              |

---

## ✏️ 주석 규칙

### 함수 주석

````kotlin
/**
 * 유저 목록 조회
 * @return 서버의 유저 리스트
 */
suspend fun getAllUsers(): List<UserResponse>
라인 주석
// 임시로 로컬 캐시 비활성화
⚙️ 코드 스타일
중괄호 {}는 개행 O
들여쓰기: 4 spaces
한 파일엔 한 클래스만
한 줄 100자 이내
하드코딩된 문자열은 strings.xml로 이동
if (isSuccess) {
    handleSuccess()
} else {
    handleError()
}
🧠 ViewModel & Repository 규칙
계층	규칙	예시
ViewModel 함수명	이벤트 중심 (UI 트리거)	onLoginClicked(), onDeleteUser()
Repository 함수명	API 행위 기반 (동사 + 명사)	getAllUsers(), createDevice()
LiveData	_ prefix로 private/protected 구분
private val _userList = MutableLiveData<List<User>>()
val userList: LiveData<List<User>> = _userList
``` |

---

## 💬 에러 & 응답 처리

```kotlin
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String?) : UiState<Nothing>()
}
viewModelScope.launch {
    _uiState.value = UiState.Loading
    try {
        val data = repository.getDevices()
        _uiState.value = UiState.Success(data)
    } catch (e: Exception) {
        _uiState.value = UiState.Error(e.message)
    }
}
🧱 커밋 컨벤션
유형	Prefix	예시
기능 추가	feat:	feat: Add getAllUsers API to AdminRepository
버그 수정	fix:	fix: Handle null response in DeviceRepository
리팩토링	refactor:	refactor: Simplify login flow
문서	docs:	docs: Add Retrofit usage guide
스타일	style:	style: Format AdminViewModel braces
🌿 브랜치 네이밍
용도	패턴	예시
기능 개발	feature/<기능명>	feature/admin-user-crud
버그 수정	fix/<이슈명>	fix/login-null-error
리팩토링	refactor/<모듈명>	refactor/network-layer
🧰 기타 규칙
?.let {} / ?: 널 처리 적극 사용
Log.d() 대신 Logger 또는 Timber 사용
약어 및 한국어 변수명 금지
함수 하나당 한 가지 책임만
ViewModel과 Repository는 UI 로직 금지
````
