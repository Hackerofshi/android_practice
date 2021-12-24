package com.shixin.http

import com.shixin.ui.jetpack.mvi.entity.ApiResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 *
 * @ProjectName:    Android_Pratice
 * @Package:        com.shixin.http
 * @ClassName:      ApiService
 * @Description:     java类作用描述
 * @Author:         shixin
 * @CreateDate:     2021/6/20 16:03
 * @UpdateUser:     shixin：
 * @UpdateDate:     2021/6/20 16:03
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
interface ApiService {


    @GET("wxarticle/chapters/json")
    suspend fun getWxArticle(): ApiResponse<List<Any>>

    @GET("abc/chapters/json")
    suspend fun getWxArticleError(): ApiResponse<List<Any>>

    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(@Field("username") userName: String, @Field("password") passWord: String): ApiResponse<Any?>

    companion object {
        const val BASE_URL = "https://wanandroid.com/"
    }
}