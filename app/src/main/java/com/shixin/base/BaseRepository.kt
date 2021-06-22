package com.shixin.base

import android.text.TextUtils
import android.util.Log
import retrofit2.Response
import com.shixin.http.Result
import java.io.IOException


open class BaseRepository {

    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>, errorMessage: String): T? {
        val result: Result<T> = safeApiResult(call, errorMessage)
        var data: T? = null

        when (result) {
            is Result.Success -> {
                data = result.data
            }
            is Result.Error -> {
                Log.d("DataRepository", "$errorMessage & Exception - ${result.exception.message}")
            }
        }

        return data
    }

    private suspend fun <T : Any> safeApiResult(
        call: suspend () -> Response<T>,
        errorMessage: String
    ): Result<T> {
        val response = call.invoke()
        if (response.isSuccessful) {
            if (response.body() is BaseResponse<*>) {
                val body: BaseResponse<Any> = response.body() as BaseResponse<Any>
                if (!TextUtils.isEmpty(body.errInfo) && body.errInfo.equals("未登录或会话已失效！")) {

                }
            }
            return Result.Success(response.body()!!)
        } else {
            /*val gson = Gson()
          val baseResponse =
              gson.fromJson(response.errorBody().toString(), BaseResponse::class.java)
          if (baseResponse.code == 1) {
              LogUtils.logi(baseResponse.errInfo)
          }*/
            Log.i("TAG", "safeApiResult: " + response.errorBody())
        }
        return Result.Error(IOException("Error Occurred during getting safe Api result, Custom ERROR - $errorMessage"))
    }
}

