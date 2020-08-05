package com.shixin.koin.datasource

import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.POST
import rx.Observable

interface RemoteDatasource {
    @POST("test")
    fun test(@Body map: Map<String, String>): Observable<Response>
}