package com.jaehong.presentation.ui.screen.search.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jaehong.domain.model.MovieItem
import com.jaehong.domain.model.MovieItems
import com.jaehong.domain.usecase.GetSearchInfoUseCase
import com.jaehong.presentation.util.checkedResult

class SearchInfoPageSource(
    private val keyword: String,
    private val getSearchInfoUseCase: GetSearchInfoUseCase,
    private val hideProgressBar: () -> Unit,
    private val checkSearchListSize: (String, Int) -> Unit,
    private val setError: () -> Unit
) : PagingSource<Int, MovieItem>() {

    override fun getRefreshKey(state: PagingState<Int, MovieItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieItem> {
        return try {
            val nextPageNumber = params.key ?: 1

            val response = getSearchInfoUseCase(keyword,nextPageNumber)
            var data: MovieItems? = null

            checkedResult(
                apiResult = response,
                success = {
                    data = it
                    hideProgressBar()
                    if(nextPageNumber == 1) checkSearchListSize(keyword,it.items.size)
                },
                error = { setError()}
            )

            return LoadResult.Page(
                data = data?.items?:throw IllegalAccessError() ,
                prevKey = if (nextPageNumber == 0) null else nextPageNumber - 1,
                nextKey = nextPageNumber + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
