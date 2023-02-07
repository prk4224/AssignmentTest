# MovieSearchApp

## 실행 방법
API KEY local.properties에서 관리
local.properties 아래 키 추가해서 실행 가능
```
CLIENT_ID_VALUE = "lbC6jcsQ7ikYCxEJh8d9"
CLIENT_SECRET_VALUE = "CyAHBA_tu0"
```

## 실행 화면

|<img src="https://user-images.githubusercontent.com/83493143/217224989-4e351c32-086a-48b7-ae97-30d38086defa.gif" width="250" />|<img src="https://user-images.githubusercontent.com/83493143/217225420-6b4445f3-5dd0-4ad5-b484-75603f2be311.gif" width="250"/>|<img src="https://user-images.githubusercontent.com/83493143/217225560-149ff145-736f-42f0-839a-7efd69e0c71a.gif" width="250" />|
|:--:|:--:|:--:|
|검색화면|클릭 시 웹뷰|프로필 수정|

|<img src="https://user-images.githubusercontent.com/83493143/217225790-faea86cb-3c8c-4fb3-b5f9-2b1216094f59.gif" width="250" />|<img src="https://user-images.githubusercontent.com/83493143/217225820-692c4f47-81b3-4b32-92f5-1799f1bab8eb.gif" width="250"/>|
|:--:|:--:|
|로그인|지도에서 채팅방 찾기|
## 사용 라이브러리
- Jatpack Compose
- Compose Navigation
- Hilt
- Room
- Retrofit
- Glide
- Paging 3
- Coroutine + Flow + TestCoroutine
- Mockk + Truth + turbine

## 멀티 모듈
### 클린 아키텍처 적용
<img width="956" alt="스크린샷 2023-02-07 오후 7 32 35" src="https://user-images.githubusercontent.com/83493143/217221111-c4c753f5-e0e8-4523-9adf-f9bddf596a55.png">



## 📌 Unit Test
- mockk 객체와 TestDispacther를 활용

### Test용 Dispatcher 와 Mock 객체 생성
```kotlin
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
}
```

### 성공 테스트
- coEvery를 활용해서 mockk 객체의 반환 값 지정
- 성공했을 때 UiState가 잘 작동하는지 확인
- 상한 대로 모든 Event가 소비 되는지 확인하기 위해서 cancelAndConsumeRemainingEvents를 활용
```kotlin
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
```

### 빈값 테스트
- coEvery를 활용해서 mockk 객체의 반환 값 지정
- 빈배열을 들어왔을 경우 UiState 잘 작동하는지 확인
- 빈배열이 들어왔을 경우 SnackBar가 잘 작동하는지 확인

```kotlin
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
```

### 실패 테스트
- coEvery를 활용해서 mockk 객체의 반환 값 지정
- 실패 했을 경우 Uistate가 Error로 잘 변화하는지 확인
```kotlin
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
```

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
