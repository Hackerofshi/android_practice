package com.shixin.ui.jetpack.mvi.mockapi

import com.shixin.ui.jetpack.mvi.repository.NewsItem

data class MockApiResponse(
    val articles: List<NewsItem>? = null
)