package com.shixin.ui.jetpack.mvi.repository

import com.shixin.ui.jetpack.mvi.mockapi.MockApi
import com.shixin.ui.jetpack.mvi.utils.PageState
import kotlinx.coroutines.delay

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
}