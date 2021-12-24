package com.shixin.ui.jetpack.mvi.repository

import com.shixin.ui.jetpack.mvi.mockapi.MockApi
import com.shixin.ui.jetpack.mvi.utils.PageState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class NewsRepository {

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: NewsRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: NewsRepository().also { instance = it }
            }
    }

    suspend fun getMockApiResponse(): PageState<List<NewsItem>> {
        val articlesApiResult = try {
            delay(2000)
            MockApi.create().getLatestNews()
        } catch (e: Exception) {
            return PageState.Error(e)
        }

        articlesApiResult.articles?.let { list ->
            return PageState.Success(data = list)
        } ?: run {
            return PageState.Error("Failed to get News")
        }
    }

    suspend fun getMockApiResponse1(): Flow<PageState<List<NewsItem>>> {
        return flow {
            try {
                val articlesApiResult = MockApi.create().getLatestNews()
                articlesApiResult.articles?.let { list ->
                    emit(PageState.Success(data = list))
                } ?: run {
                    // emit(PageState.Error("Failed to get News"))
                    emit(PageState.Error<List<NewsItem>>("Failed to get News"))
                }
            } catch (e: Exception) {
                emit(PageState.Error<List<NewsItem>>(e))
            }
        }.flowOn(Dispatchers.IO)
    }
}