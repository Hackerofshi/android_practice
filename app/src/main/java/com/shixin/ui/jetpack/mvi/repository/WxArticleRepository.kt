package com.shixin.ui.jetpack.mvi.repository

import com.shixin.ui.jetpack.mvi.base.BaseRepository
import com.shixin.ui.jetpack.mvi.entity.ApiResponse
import com.shixin.ui.jetpack.mvi.entity.ApiSuccessResponse
import com.shixin.ui.jetpack.mvi.mockapi.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WxArticleRepository : BaseRepository() {

    private val mService by lazy {
        RetrofitClient.service
    }

    suspend fun fetchWxArticleFromNet(): ApiResponse<List<Any>> {
        return executeHttp {
            mService.getWxArticle()
        }
    }

    suspend fun fetchWxArticleFromDb(): ApiResponse<List<Any>> {
        return getWxArticleFromDatabase()
    }

    suspend fun fetchWxArticleError(): ApiResponse<List<Any>> {
        return executeHttp {
            mService.getWxArticleError()
        }
    }

    suspend fun login(username: String, password: String): ApiResponse<Any?> {
        return executeHttp {
            mService.login(username, password)
        }
    }

    private suspend fun getWxArticleFromDatabase(): ApiResponse<List<Any>> = withContext(
        Dispatchers.IO) {
        val bean = Any()
//        bean.id = 999
//        bean.name = "零先生"
//        bean.visible = 1
        ApiSuccessResponse(arrayListOf(bean))
    }


}