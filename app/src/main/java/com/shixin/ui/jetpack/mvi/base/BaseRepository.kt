package com.shixin.ui.jetpack.mvi.base

import android.util.Log
import com.shixin.App.Companion.instance
import com.shixin.BuildConfig
import com.shixin.base.BaseResponse
import com.shixin.http.SPUtils
import com.shixin.ui.jetpack.mvi.entity.*
import kotlinx.coroutines.delay
import retrofit2.Response
import java.io.IOException


open class BaseRepository {

    suspend fun <T> executeHttp(block: suspend () -> ApiResponse<T>): ApiResponse<T> {
        //for test
        delay(500)
        runCatching {
            block.invoke()
        }.onSuccess { data: ApiResponse<T> ->
            return handleHttpOk(data)
        }.onFailure { e ->
            return handleHttpError(e)
        }
        return ApiEmptyResponse()
    }

    /**
     * 非后台返回错误，捕获到的异常
     */
    private fun <T> handleHttpError(e: Throwable): ApiErrorResponse<T> {
        if (BuildConfig.DEBUG) e.printStackTrace()
        handlingExceptions(e)
        return ApiErrorResponse(e)
    }

    /**
     * 返回200，但是还要判断isSuccess
     */
    private fun <T> handleHttpOk(data: ApiResponse<T>): ApiResponse<T> {
        return if (data.isSuccess) {
            getHttpSuccessResponse(data)
        } else {
            handlingApiExceptions(data.errorCode, data.errorMsg)
            ApiFailedResponse(data.errorCode, data.errorMsg)
        }
    }

    /**
     * 成功和数据为空的处理
     */
    private fun <T> getHttpSuccessResponse(response: ApiResponse<T>): ApiResponse<T> {
        val data = response.data
        return if (data == null || data is List<*> && (data as List<*>).isEmpty()) {
            ApiEmptyResponse()
        } else {
            ApiSuccessResponse(data)
        }
    }


    suspend fun <T : Any> safeApiCall(
        call: suspend () -> Response<T>,
        errorMessage: String
    ): T {
        try {
            val result: Result<T> = safeApiResult(call, errorMessage)
            var data: T? = null

            when (result) {
                is Result.Success -> {
                    data = result.data
                }
                is Result.Error -> {
                    val response = BaseResponse<Any>(
                        -1,
                        " ${result.exception.message}",
                        Any()
                    )
                    data = response as T
                    Log.d("1.DataRepository", "$errorMessage & Exception - ${result.exception}")
                }
            }
            return data
        } catch (e: Exception) {
            Log.i("TAG", "safeApiCall: ${e.message}")
            val response = BaseResponse<Any>(
                -1,
                "$errorMessage ${e.message}",
                Any()
            )
            val data = response as T
            return data
        }
    }

    private suspend fun <T : Any> safeApiResult(
        call: suspend () -> Response<T>,
        errorMessage: String
    ): Result<T> {
        val response = call.invoke()
        if (response.isSuccessful) {
            if (response.body() is BaseResponse<*>) {
                Log.d(
                    "1.BaseResponse",
                    "$errorMessage & Exception - ${response.code()} ${response.message()}"
                )
                val body = response.body() as BaseResponse<*>
                if (body.code == 401) {
                    SPUtils.setSharedBooleanData(instance, "login", false)
                    // RxBus.getDefault().post(EventEntity(Constant.EXIT))
                }
            }
            return Result.Success(response.body()!!)
        } else {
            Log.d(
                "1.DataRepository",
                "$errorMessage & Exception - ${response.code()} ${response.message()}"
            )
            return Result.Error(IOException("$errorMessage ${response.code()} ${response.message()}"))
        }
    }

}