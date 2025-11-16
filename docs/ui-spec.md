# UI-SPEC.md

Palmprint Recognition – User & Admin UI Flow Specification
(UC 기반 정리)

---

# 1. 유저 흐름 (User Flows)

## 1-1. UC-1, UC-3 – 회원가입 / 로그인

### **파일**

* `ui.user.UserLoginActivity.kt`
* `ui.user.UserSignUpActivity.kt`
* `ui.user.modules.TermsAgreementActivity.kt`
* `ui.user.modules.SignUpCompleteActivity.kt`

### **플로우**

```
UserLoginActivity (UC-3: 로그인)
    ├─ [로그인 버튼] → UserMainActivity
    └─ [회원가입 버튼] → UserSignUpActivity

UserSignUpActivity (UC-1)
    └─ [다음 버튼] → TermsAgreementActivity

TermsAgreementActivity
    └─ [동의하고 가입] → SignUpCompleteActivity

SignUpCompleteActivity
    └─ [메인으로] → UserLoginActivity
```

---

## 1-2. UC-4 – 로그아웃

### **파일**

* `ui.user.UserMainActivity.kt`

### **플로우**

```
UserMainActivity
    └─ [로그아웃 버튼] → UserLoginActivity (세션 종료)
```

---

## 1-3. UC-5, UC-6, UC-13 – 손바닥 등록/삭제 + 디바이스 촬영

### **파일**

* `ui.user.modules.PalmRegisterActivity.kt`
* `activity_palm_register.xml`

### **플로우**

```
UserMainActivity
    └─ [손바닥 등록/삭제] → PalmRegisterActivity
        ├─ [손바닥 등록] → 디바이스 촬영 → 서버 전송 (UC-5, UC-13)
        └─ [손바닥 삭제] → 서버 임베딩 삭제 (UC-6)
```

---

## 1-4. UC-7, UC-8, UC-9 – 인증 기관 관리

### **파일**

* `ui.user.modules.InstitutionListActivity.kt`
* `activity_institution_list.xml`

### **플로우**

```
UserMainActivity
    └─ [기관 관리] → InstitutionListActivity
        ├─ 화면 진입 → 기관 리스트 조회 (UC-7)
        ├─ [추가] → 기관명/ID 입력 후 저장 (UC-8)
        └─ [삭제] 또는 롱클릭 → 특정 기관 삭제 (UC-9)
```

---

## 1-5. UC-10, UC-11, UC-12 – 결제 수단 관리

### **파일**

* `ui.user.modules.PaymentListActivity.kt`
* `activity_payment_list.xml`

### **플로우**

```
UserMainActivity
    └─ [결제 수단 관리] → PaymentListActivity
        ├─ 화면 진입 → 결제 수단 조회 (UC-10)
        ├─ [추가] → 카드사/카드번호 입력 후 저장 (UC-11)
        └─ [삭제] → 특정 결제 수단 삭제 (UC-12)
```

---

## 1-6. UC-14, UC-15 – 인증 내역 조회/신고

### **파일**

* `ui.user.modules.HistoryListActivity.kt`
* `activity_history_list.xml`

### **플로우**

```
UserMainActivity
    └─ [인증 내역 조회] → HistoryListActivity
        ├─ 인증 내역 리스트 표시 (UC-14)
        └─ [신고] → 신고 폼 입력 후 제출 (UC-15)
```

---

# 2. 관리자 흐름 (Admin Flows)

## 2-1. UC-22, UC-23 – 관리자 로그인/로그아웃

### **파일**

* `ui.admin.AdminLoginActivity.kt`
* `ui.admin.AdminMainActivity.kt`

### **플로우**

```
UserLoginActivity
    └─ [관리자 로그인] → AdminLoginActivity

AdminLoginActivity (UC-22)
    └─ [로그인] → AdminMainActivity

AdminMainActivity (UC-23)
    └─ [로그아웃] → AdminLoginActivity
```

---

## 2-2. UC-24 – 관리자: 유저 정보 관리

### **파일**

* `ui.admin.modules.user.AdminUserListActivity.kt`
* `ui.admin.modules.user.AdminUserEditActivity.kt`
* `ui.admin.modules.user.AdminUserAddActivity.kt`
* `ui.admin.modules.user.AdminUserDeleteDialog.kt`

### **플로우**

```
AdminDashboardActivity
    └─ [유저 관리] → AdminUserListActivity

AdminUserListActivity (UC-24)
    ├─ 리스트 조회
    ├─ [유저 아이템 클릭] → AdminUserEditActivity (수정)
    └─ [추가 버튼] → AdminUserAddActivity (등록)

AdminUserEditActivity
    ├─ [저장] → finish() → AdminUserListActivity
    └─ [삭제] → AdminUserDeleteDialog → 삭제 시 목록으로
```

---

## 2-3. UC-25 – 관리자: 손바닥 임베딩 관리

### **파일**

* `ui.admin.modules.PalmManageActivity.kt`

### **플로우**

```
AdminDashboardActivity
    └─ [손바닥 관리] → PalmManageActivity
        ├─ 손바닥 임베딩 리스트 조회
        ├─ [등록]
        └─ [삭제]
```

---

## 2-4. UC-26 – 디바이스 관리

### **파일**

* `ui.admin.modules.DeviceManageActivity.kt`

### **플로우**

```
AdminDashboardActivity
    └─ [디바이스 관리] → DeviceManageActivity
        ├─ 디바이스 목록 조회
        ├─ [등록]
        ├─ [수정]
        └─ [삭제]
```

---

## 2-5. UC-27, UC-28 – 통계/디바이스 상태 조회

### **파일**

* `ui.admin.modules.StatsDashboardActivity.kt`

### **플로우**

```
AdminDashboardActivity
    └─ [통계 정보] → StatsDashboardActivity
        ├─ 유저 수, 디바이스 수
        ├─ 손바닥 등록 수
        ├─ 인증 요청 수 / 성공률
        └─ 디바이스 상태 (UC-28)
```

---

# 3. 전체 구조 요약

```
UserLoginActivity
 ├─ UserSignUpActivity → TermsAgreementActivity → SignUpCompleteActivity
 ├─ UserMainActivity
 │    ├─ PalmRegisterActivity
 │    ├─ InstitutionListActivity
 │    ├─ PaymentListActivity
 │    └─ HistoryListActivity
 └─ AdminLoginActivity → AdminMainActivity
      └─ AdminDashboardActivity
           ├─ AdminUserListActivity → AdminUserEditActivity / AdminUserAddActivity
           ├─ DeviceManageActivity
           ├─ PalmManageActivity
           └─ StatsDashboardActivity
```
