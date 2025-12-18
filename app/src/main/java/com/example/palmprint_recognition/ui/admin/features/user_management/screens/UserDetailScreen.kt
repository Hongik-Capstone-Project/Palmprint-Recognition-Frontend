package com.example.palmprint_recognition.ui.admin.features.user_management.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.palmprint_recognition.data.model.AdminUserDetail
import com.example.palmprint_recognition.data.model.UserInstitutionSimple
import com.example.palmprint_recognition.ui.admin.features.user_management.viewmodel.UserDetailViewModel
import com.example.palmprint_recognition.ui.admin.navigation.AdminRoutes
import com.example.palmprint_recognition.ui.common.button.SingleCenterButton
import com.example.palmprint_recognition.ui.common.checkbox.CheckBox
import com.example.palmprint_recognition.ui.common.field.LabeledField
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.HeaderContainer
import com.example.palmprint_recognition.ui.common.layout.RootLayout
import com.example.palmprint_recognition.ui.common.table.TableColumn
import com.example.palmprint_recognition.ui.common.table.TableView
import com.example.palmprint_recognition.ui.core.state.UiState

/**
 * 유저 상세 Screen
 */
@Composable
fun UserDetailScreen(
    userId: Int,
    navController: NavController,
    onDeleteClick: () -> Unit,
    viewModel: UserDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(userId) {
        viewModel.loadUser(userId)
    }

    val onBackToList: () -> Unit = {
        navController.navigate(AdminRoutes.USER_LIST) {
            popUpTo(AdminRoutes.USER_LIST) { inclusive = true }
        }
    }

    BackHandler { onBackToList() }

    UserDetailContent(
        uiState = uiState,
        onRetry = { viewModel.loadUser(userId) },
        onDeleteClick = onDeleteClick,
        onBackToList = onBackToList
    )
}

/**
 * UI Only (Preview 가능)
 */
@Composable
private fun UserDetailContent(
    uiState: UiState<AdminUserDetail>,
    onRetry: () -> Unit,
    onDeleteClick: () -> Unit,
    onBackToList: () -> Unit
) {
    RootLayout(
        headerWeight = 2f,
        bodyWeight = 6.5f,
        footerWeight = 1.5f,
        sectionGapWeight = 0.4f,

        header = { HeaderContainer() },

        body = {
            when (uiState) {
                UiState.Idle -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("사용자 정보를 준비 중...")
                    }
                }

                UiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is UiState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = uiState.message ?: "오류가 발생했습니다.")
                        Spacer(modifier = Modifier.height(12.dp))
                        SingleCenterButton(text = "다시 시도", onClick = onRetry)
                        Spacer(modifier = Modifier.height(8.dp))
                        SingleCenterButton(text = "목록으로", onClick = onBackToList)
                    }
                }

                is UiState.Success -> {
                    val user = uiState.data
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp)
                    ) {
                        UserInfoFieldSection(user = user)

                        Spacer(modifier = Modifier.height(16.dp))

                        InstitutionListTableSection(
                            institutions = user.userInstitutions,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        AdminAccountCheckSection(
                            isAdmin = false, // TODO: 상세 API에 is_admin 내려오면 연결
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        },

        footer = {
            // ✅ 성공 상태일 때만 삭제 버튼 노출 (그 외에는 비워둠)
            if (uiState is UiState.Success) {
                Footer {
                    SingleCenterButton(
                        text = "삭제",
                        onClick = onDeleteClick
                    )
                }
            } else {
                Footer { /* empty */ }
            }
        }
    )
}

/**
 * 유저 정보 필드 Section (읽기 전용)
 */
@Composable
private fun UserInfoFieldSection(
    user: AdminUserDetail
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "유저 정보")

        LabeledField(
            label = "이름",
            value = user.name,
            onValueChange = {},
            readOnly = true,
            enabled = false
        )

        LabeledField(
            label = "이메일",
            value = user.email,
            onValueChange = {},
            readOnly = true,
            enabled = false
        )

        LabeledField(
            label = "손바닥 등록 여부",
            value = if (user.isPalmRegistered) "O" else "X",
            onValueChange = {},
            readOnly = true,
            enabled = false
        )
    }
}

/**
 * 인증 기관 목록 테이블 Section (읽기 전용)
 */
@Composable
private fun InstitutionListTableSection(
    institutions: List<UserInstitutionSimple>,
    modifier: Modifier = Modifier
) {
    val columns = listOf(TableColumn(title = "기관명", weight = 1f))
    val rows = institutions.map { listOf(it.institutionName) }

    Box(modifier = modifier) {
        TableView(
            title = "인증 기관 목록",
            columns = columns,
            rows = rows,
            hasMoreData = false,
            isLoading = false,
            modifier = Modifier.fillMaxSize(),
            onRowClick = {},
            onLoadMore = {}
        )
    }
}

/**
 * 관리자 계정 체크 (읽기 전용)
 */
@Composable
private fun AdminAccountCheckSection(
    isAdmin: Boolean,
    modifier: Modifier = Modifier
) {
    CheckBox(
        checked = isAdmin,
        text = "관리자 계정",
        readOnly = true,
        modifier = modifier,
        onCheckedChange = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewUserDetailContent_Success() {
    UserDetailContent(
        uiState = UiState.Success(
            AdminUserDetail(
                id = 1,
                name = "Alice",
                email = "alice@example.com",
                isPalmRegistered = true,
                userInstitutions = listOf(
                    UserInstitutionSimple("inst_112", "Hongik University"),
                    UserInstitutionSimple("inst_113", "LG")
                )
            )
        ),
        onRetry = {},
        onDeleteClick = {},
        onBackToList = {}
    )
}
