package com.jaehong.presentation

import android.accounts.NetworkErrorException
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.Event
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.jaehong.domain.model.ApiResult
import com.jaehong.domain.model.MovieItem
import com.jaehong.domain.model.MovieItems
import com.jaehong.domain.model.UiStateResult
import com.jaehong.domain.usecase.GetSearchInfoUseCase
import com.jaehong.presentation.navigation.SearchAppNavigatorImpl
import com.jaehong.presentation.ui.screen.search.SearchViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.DisplayName

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val getSearchInfoUseCase: GetSearchInfoUseCase = mockk()
    private val navigator: SearchAppNavigatorImpl = mockk()
    private val savedStateHandle = SavedStateHandle()

    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private val viewModel: SearchViewModel by lazy { SearchViewModel(getSearchInfoUseCase,navigator,savedStateHandle) }

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    @DisplayName("[성공] 검색 결과가 있을 경우 UiState가 Loading 에서 Success로 바뀐다.")
    fun getSearchListSuccess() = runTest {
        // given
        coEvery {
            getSearchInfoUseCase(TEST_KEYWORD, 1)
        } returns ApiResult.Success(TEXT_MOVIE_ITEMS)

        //when
        viewModel.showProgressBar()

        //then
        viewModel.uiState.test {
            viewModel.getSearchList(TEST_KEYWORD)
            assertThat(cancelAndConsumeRemainingEvents()).containsExactly(
                Event.Item(UiStateResult.LOADING),
                Event.Item(UiStateResult.SUCCESS),
            )
        }
    }

    @Test
    @DisplayName("[빈배열] 검색 결과가 없을 경우 uiState 가 Loaging 에서 Success로 바뀐 후 SnackBar가 띄어진다.")
    fun getSearchListEmpty() = runTest {
        // given
        coEvery {
            getSearchInfoUseCase(TEST_KEYWORD, 1)
        } returns ApiResult.Success(MovieItems(emptyList()))

        //when
        viewModel.showProgressBar()

        //then
        viewModel.uiState.test {
            viewModel.getSearchList(TEST_KEYWORD)
            assertThat(cancelAndConsumeRemainingEvents()).containsExactly(
                Event.Item(UiStateResult.LOADING),
                Event.Item(UiStateResult.SUCCESS),
            )
        }

        viewModel.snackbarState.test {
            viewModel.getSearchList(TEST_KEYWORD)
            assertThat(cancelAndConsumeRemainingEvents()).containsExactly(
                Event.Item(true),
            )
        }
    }

    @Test
    @DisplayName("[실패] Api 요청이 실패할 경우 uiState를 Error로 만든다.")
    fun getSearchListError() = runTest {
        // given
        coEvery {
            getSearchInfoUseCase(TEST_KEYWORD, 1)
        } returns ApiResult.Error(NetworkErrorException("Network Connect Error"))

        //when
        viewModel.showProgressBar()

        //then
        viewModel.uiState.test {
            viewModel.getSearchList(TEST_KEYWORD)
            assertThat(cancelAndConsumeRemainingEvents()).containsExactly(
                Event.Item(UiStateResult.LOADING),
                Event.Item(UiStateResult.ERROR),
            )
        }
    }

    companion object {
        private const val TEST_KEYWORD = "안녕"
        private val TEXT_MOVIE_ITEMS = MovieItems(
            listOf(MovieItem("","","","",""))
        )
    }
}