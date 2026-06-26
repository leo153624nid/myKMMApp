package com.example.mykmmapp.postFeature.presentation

import app.cash.turbine.test
import com.example.mykmmapp.postFeature.data.model.Post
import com.example.mykmmapp.postFeature.data.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class PostsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var mockRepository: MockPostRepository
    private lateinit var sut: PostsViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockRepository = MockPostRepository()
        sut = PostsViewModel(mockRepository)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() {
        val state = sut.state.value

        assertFalse(state.isLoading)
        assertFalse(state.isLoadingMore)
        assertNull(state.error)
        assertTrue(state.canLoadMore)
        assertEquals(1, state.currentPage)
        assertFalse(state.offlineMode)
    }

    @Test
    fun `refreshPosts success`() = runTest {
        val expected: List<Post> = listOf(
            Post(
                id = 1,
                userId = 111,
                title = "title1",
                body = "body1"
            ),
            Post(
                id = 2,
                userId = 111,
                title = "title2",
                body = "body2"
            ),
        )
        mockRepository.stubGetPosts = Result.success(expected)

        sut.state.test {
            val initial = awaitItem()
            assertFalse(initial.isLoading)
            assertTrue(initial.posts.isEmpty())

            sut.handleIntent(PostsIntent.Refresh)

            val loading = awaitItem()
            assertTrue(loading.isLoading)

            val loaded = awaitItem()
            assertFalse(loaded.isLoading)
            assertEquals(expected.size, loaded.posts.size)
            assertEquals(expected.first().id, loaded.posts.first().id)
            assertEquals(mockRepository.getPostsCallCount, 2)
            assertEquals(mockRepository.cachePostsCallCount, 2)
        }
    }

    @Test
    fun `refreshPosts failure`() = runTest {
        val expected = "some error"
        mockRepository.stubGetPosts = Result.failure(Exception("some error"))

        sut.state.test {
            val initial = awaitItem()
            assertFalse(initial.isLoading)
            assertTrue(initial.posts.isEmpty())

            sut.handleIntent(PostsIntent.Refresh)

            val loading = awaitItem()
            assertTrue(loading.isLoading)

            val loaded = awaitItem()
            assertFalse(loaded.isLoading)
            assertTrue(loaded.posts.isEmpty())
            assertEquals(mockRepository.getPostsCallCount, 2)
            assertEquals(mockRepository.cachePostsCallCount, 1)
            assertEquals(expected, loaded.error)
        }
    }

    @Test
    fun `loadNextPage success`() = runTest {
        val pageOne: List<Post> = listOf(
            Post(
                id = 1,
                userId = 111,
                title = "title1",
                body = "body1"
            ),
            Post(
                id = 2,
                userId = 111,
                title = "title2",
                body = "body2"
            ),
        )
        mockRepository.stubGetPosts = Result.success(pageOne)

        sut.state.test {
            sut.handleIntent(PostsIntent.Refresh)
            awaitItem()
            awaitItem()
            val initial = awaitItem()
            assertFalse(initial.isLoadingMore)
            assertTrue(initial.posts.size == 2)
            assertTrue(initial.currentPage == 1)
            assertTrue(initial.canLoadMore)

            sut.handleIntent(PostsIntent.LoadNextPage)

            val loading = awaitItem()
            assertTrue(loading.isLoadingMore)

            val loaded = awaitItem()
            assertFalse(loaded.isLoadingMore)
            assertEquals(loaded.posts.size, (pageOne.size + pageOne.size))
            assertEquals(mockRepository.getPostsCallCount,3)
            assertEquals(mockRepository.cachePostsCallCount, 3)
            assertNull(loaded.error)
            assertTrue(loaded.canLoadMore)
            assertEquals(loaded.currentPage, 2)
        }
    }

    @Test
    fun `loadNextPage failure`() = runTest {
        val pageOne: List<Post> = listOf(
            Post(
                id = 1,
                userId = 111,
                title = "title1",
                body = "body1"
            ),
            Post(
                id = 2,
                userId = 111,
                title = "title2",
                body = "body2"
            ),
        )
        mockRepository.stubGetPosts = Result.success(pageOne)

        sut.state.test {
            sut.handleIntent(PostsIntent.Refresh)
            awaitItem()
            awaitItem()
            val initial = awaitItem()
            val expected = "some error"
            mockRepository.stubGetPosts = Result.failure(Exception("some error"))
            assertFalse(initial.isLoadingMore)
            assertTrue(initial.posts.size == 2)
            assertTrue(initial.currentPage == 1)
            assertTrue(initial.canLoadMore)

            sut.handleIntent(PostsIntent.LoadNextPage)

            val loading = awaitItem()
            assertTrue(loading.isLoadingMore)

            val loaded = awaitItem()
            assertFalse(loaded.isLoadingMore)
            assertEquals(loaded.currentPage, 1)
            assertTrue(loaded.canLoadMore)
            assertEquals(mockRepository.getPostsCallCount,3)
            assertEquals(mockRepository.cachePostsCallCount, 2)
            assertEquals(expected, loaded.error)
        }
    }

    @Test
    fun `postClicked`() = runTest {
        val post = Post(
            id = 11,
            userId = 222,
            title = "title",
            body = "body"
        )

        sut.effect.test {
            sut.handleIntent(PostsIntent.PostClicked(post))

            val effect = awaitItem()
            assertIs<PostsEffect.NavigateToDetail>(effect)
            assertEquals(11, effect.postId)
        }
    }

}

class MockPostRepository: PostRepository {

    override val pageSize: Int = 1

    var stubGetPosts: Result<List<Post>> = Result.success(emptyList())
    var stubGetPost: Result<Post> = Result.success(Post(
        id = 1,
        userId = 111,
        title = "title",
        body = "body"
    ))
    var stubGetChachedPosts: List<Post> = emptyList()
    var getPostsCallCount = 0
    var getPostCallCount = 0
    var getCachedPostsCallCount = 0
    var cachePostsCallCount = 0
    var clearCacheCallCount = 0

    override suspend fun getPosts(page: Int): Result<List<Post>> {
        getPostsCallCount++
        return stubGetPosts
    }

    override suspend fun getPost(id: Int): Result<Post> {
        getPostCallCount++
        return stubGetPost
    }

    override suspend fun getCachedPosts(): Flow<List<Post>> {
        getCachedPostsCallCount++
        return flow { stubGetChachedPosts }
    }

    override suspend fun cachePosts(posts: List<Post>) {
        cachePostsCallCount++
        // TODO
    }

    override suspend fun clearCache() {
        clearCacheCallCount++
        // TODO
    }

}