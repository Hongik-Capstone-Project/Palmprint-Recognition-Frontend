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
import com.example.palmprint_recognition.data.model.AdminUserInstitution
import com.example.palmprint_recognition.ui.admin.features.user_management.viewmodel.UserDetailViewModel
import com.example.palmprint_recognition.ui.admin.navigation.AdminRoutes
import com.example.palmprint_recognition.ui.common.button.SingleCenterButton
import com.example.palmprint_recognition.ui.common.field.LabeledField
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.Header
import com.example.palmprint_recognition.ui.common.layout.HeaderContainer
import com.example.palmprint_recognition.ui.common.layout.RootLayoutScrollable
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
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(userId) {
        viewModel.loadUser(userId)
    }

    when (val ui = state) {
        UiState.Idle -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("사용자 정보를 준비 중...")
        }

        UiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        is UiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("오류 발생: ${ui.message}")
        }

        is UiState.Success -> {
            UserDetailContent(
                user = ui.data,
                onDeleteClick = onDeleteClick
            )
        }
    }

    BackHandler {
        navController.navigate(AdminRoutes.USER_LIST) {
            popUpTo(AdminRoutes.USER_LIST) { inclusive = true }
        }
    }
}

/**
 * 유저 상세 UI (Scrollable)
 * - 헤더/바디/푸터 전부 같이 스크롤
 * - 테이블은 모두 한 번에 표시
 */
@Composable
private fun UserDetailContent(
    user: AdminUserDetail,
    onDeleteClick: () -> Unit
) {
    RootLayoutScrollable(
        sectionGap = 12.dp,

        header = {
            HeaderContainer()
        },

        body = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                UserInfoFieldSection(
                    user = user,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                InstitutionListTableSectionAll(
                    institutions = user.userInstitutions,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
        },

        footer = {
            Footer {
                SingleCenterButton(
                    text = "삭제",
                    onClick = onDeleteClick
                )
            }
        }
    )
}

/**
 * 유저 정보 필드 Section (읽기 전용)
 */
@Composable
private fun UserInfoFieldSection(
    user: AdminUserDetail,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
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
 * 인증 기관 목록 테이블 Section (전부 출력 / 페이지네이션 삭제)
 */
@Composable
private fun InstitutionListTableSectionAll(
    institutions: List<AdminUserInstitution>,
    modifier: Modifier = Modifier
) {
    val columns = listOf(
        TableColumn(title = "기관명", weight = 1f)
    )

    val rows = institutions.map { inst ->
        listOf(inst.institution.name)
    }

    Box(modifier = modifier) {
        TableView(
            title = "인증 기관 목록",
            columns = columns,
            rows = rows,
            hasMoreData = false, // 더보기 없음
            isLoading = false,
            modifier = Modifier.fillMaxWidth(), // 높이 강제하지 않음
            onRowClick = {},     // 읽기 전용
            onLoadMore = {}      // 호출되지 않음 (안전하게 빈 함수)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewUserDetailContent() {
    UserDetailContent(
        user = AdminUserDetail(
            id = 1,
            name = "Alice",
            email = "alice@example.com",
            createdAt = "2025-12-18T09:09:22.503Z",
            isPalmRegistered = true,
            paymentMethods = emptyList(),
            reports = emptyList(),
            userInstitutions = listOf(
                AdminUserInstitution(
                    id = 1,
                    createdAt = "2025-12-18T09:09:22.504Z",
                    userId = 1,
                    institution = com.example.palmprint_recognition.data.model.AdminInstitution(
                        id = 10,
                        createdAt = "2025-12-18T09:09:22.504Z",
                        name = "Hongik University",
                        address = null
                    ),
                    institutionUserId = "inst_112"
                ),
                AdminUserInstitution(
                    id = 2,
                    createdAt = "2025-12-18T09:09:22.504Z",
                    userId = 1,
                    institution = com.example.palmprint_recognition.data.model.AdminInstitution(
                        id = 11,
                        createdAt = "2025-12-18T09:09:22.504Z",
                        name = "LG",
                        address = null
                    ),
                    institutionUserId = "inst_113"
                )
            ),
            userInstitutionRoles = emptyList()
        ),
        onDeleteClick = {}
    )
}
