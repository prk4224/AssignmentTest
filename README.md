# MovieSearchApp


## 🚀 트러블 슈팅

### Paging 3 Library 사용중 트러블 슈팅

#### 문제점 
: 기존에 API를 통해서 data를 가져오는 형식이 Flow로 되어있었다. 
하지만 Paging 3을 사용하면 반환 값이 필요한데, 
비동기로 동작하는 Flow를 원하는 시점에 반화하지 못하므로 가저온 정보들이 제데로 갱신되지 않았다.

#### 기존 방식
먼저, data를 가져올 PageSource를 구현한다.
```kotlin
class SearchInfoPageSource(
    private val getSearchInfoList: (Int) -> List<MovieItem>,
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

            val response = getSearchInfoList(nextPageNumber)

            return LoadResult.Page(
                data = response,
                prevKey = if (nextPageNumber == 0) null else nextPageNumber - 1,
                nextKey = nextPageNumber + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
```

ViewModel 로직
```kotlin
  fun setSearchList(keyword: String) {
        searchList = Pager(
            PagingConfig(pageSize = 10)
        ) { SearchInfoPagingSource {
            getSearchList(keyword,it)
        }}.flow.cachedIn(viewModelScope)
    }


 fun getSearchList(keyword: String, page: Int): List<MovieItem> {
        val result: List<MovieItem>? = null
        viewModelScope.launch {
            getSearchInfoUseCase(keyword).collectLatest {
                result = checkedResult(
                    apiResult = it,
                    success = { data ->
                        checkSearchListSize(data.items.size,keyword)
                        hideProgressBar()
                    }
                )
            }
        }
        return result ?: NullPointException()
    }
```

**Scope 안에서 동작은 비동기로 일어나기 때문에 Null Point가 떨어지거나 제데로 데이터가 갱신되지 않는 다는 문제점이 생겼다.**

### 🚩 해결 방법
: Flow를 사용해서 반환시점을 제데로 컨트롤되지 않아서 Flow를 사용하지 않는 것이 맞다고 판단.


```kotlin
// 검색어, UseCase 등 필요한 함수들을 람다 형식을 구현
class SearchInfoPageSource(
    private val keyword: String,
    private val getSearchInfoUseCase: GetSearchInfoUseCase,
    private val hideProgressBar: () -> Unit,
    private val checkSearchListSize: (String, Int) -> Unit
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
                }
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

// ViewModel
// ViewModel 바로 Paging을 하도록 
fun setSearchList(keyword: String) {
        searchList = Pager(
            PagingConfig(pageSize = 10)
        ) { SearchInfoPageSource(
            keyword = keyword,
            getSearchInfoUseCase = getSearchInfoUseCase,
            hideProgressBar = { hideProgressBar() },
            checkSearchListSize = { keyword, size ->
                checkSearchListSize(size,keyword)
            }
        ) }.flow.cachedIn(viewModelScope)
    }
```
### 💡 고민한 부분
: PageSource는 어디에다가 구현 할 것인가 ?
- data Module ?
: 엄연히 말하면 Paging 자체가 data를 들고 오는 행위 인것은 맞지만, 얼마나 들고올지 판단해야하는 것은 data Module에 맞지 않는다고 판단.

- domain Module ?
: 구글의 Paging 라이브러리 공식 문서에는 PagingSource를 Repository에서 생성한 뒤, Pager를 통해 PagingData를 가져오게끔 아키텍처가 되어 있다. 
하지만 Android 의존성 있는 Pager 같은 경우 domain에 있는 것이 옳지 않다고 생각했습니다.
(많은 Clean Architecture를 검색 해본 결과 Room 같은 Android 의존성이 있는 라이브러리도 domain에 들어가있는 경우도 있지만 Clean Architecture에는 맞지 않는다고 생각했습니다.)

- **presenter Module ?**
: Paging을 얼마나 들고올지 판단하고 명령을 하는 부분은 presenter Module에서 하는 것이 맞다고 판단.
