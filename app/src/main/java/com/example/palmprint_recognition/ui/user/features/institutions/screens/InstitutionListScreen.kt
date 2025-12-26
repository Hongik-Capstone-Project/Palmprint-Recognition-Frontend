package com.example.palmprint_recognition.ui.user.features.institutions.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.data.model.UserInstitution
import com.example.palmprint_recognition.ui.common.button.SingleCenterButton
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.HeaderContainer
import com.example.palmprint_recognition.ui.common.layout.RootLayoutScrollable
import com.example.palmprint_recognition.ui.common.table.TableColumn
import com.example.palmprint_recognition.ui.common.table.TableView
import com.example.palmprint_recognition.ui.core.state.UiState
import com.example.palmprint_recognition.ui.user.features.institutions.viewmodel.InstitutionListViewModel

@Composable
fun InstitutionListScreen(
    onInstitutionClick: (Int) -> Unit,  // institutionId 전달 (DeleteInstitution으로)
    onAddInstitutionClick: () -> Unit,
    viewModel: InstitutionListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadInstitutions()
    }

    when (val ui = state) {
        UiState.Idle -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("인증기관 정보를 준비 중...")
        }

        UiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        is UiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("오류 발생: ${ui.message}")
        }

        is UiState.Success -> {
            InstitutionListContent(
                institutions = ui.data,
                onInstitutionClick = onInstitutionClick,
                onAddInstitutionClick = onAddInstitutionClick
            )
        }
    }
}

@Composable
private fun InstitutionListContent(
    institutions: List<UserInstitution>,
    onInstitutionClick: (Int) -> Unit,
    onAddInstitutionClick: () -> Unit
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
                InstitutionListTableSectionAll(
                    institutions = institutions,
                    modifier = Modifier.fillMaxWidth(),
                    onInstitutionClick = onInstitutionClick
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
        },

        footer = {
            Footer {
                SingleCenterButton(
                    text = "내 인증기관 추가",
                    onClick = onAddInstitutionClick
                )
            }
        }
    )
}

/**
 * 인증 기관 목록 테이블 Section (전부 출력 / 스크롤 없음)
 * - row 클릭 시 해당 기관 삭제 화면으로 이동
 */
@Composable
private fun InstitutionListTableSectionAll(
    institutions: List<UserInstitution>,
    modifier: Modifier = Modifier,
    onInstitutionClick: (Int) -> Unit
) {
    val columns = listOf(
        TableColumn(title = "기관명", weight = 1f)
    )

    val rows = institutions.map { ui ->
        listOf(ui.institution.name)
    }

    Box(modifier = modifier) {
        TableView(
            title = "내 인증 기관 목록",
            columns = columns,
            rows = rows,
            hasMoreData = false,
            isLoading = false,
            modifier = Modifier.fillMaxWidth(),
            onRowClick = { index ->
                // 백엔드 delete는 institution_id(= Institution.id)로 삭제함
                val institutionId = institutions.getOrNull(index)?.institution?.id ?: return@TableView
                onInstitutionClick(institutionId)
            },
            onLoadMore = {}, // 호출되지 않음 (안전)
            scrollEnabled = false
        )
    }
}
