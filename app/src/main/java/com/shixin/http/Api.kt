package com.shixin.http

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log
import android.util.SparseArray
import androidx.annotation.NonNull
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.shixin.App.Companion.instance
import io.reactivex.Observable
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit


const val AUTHORIZED = "AUTHORIZED"
const val COOKIE = "cookie"

object Api {

    //读超时长，单位：毫秒
    private const val READ_TIME_OUT = 600 * 1000

    //连接时长，单位：毫秒
    private const val CONNECT_TIME_OUT = 600 * 1000

    private val sRetrofitManager: SparseArray<Api> = SparseArray<Api>(2)


    /*************************缓存设置*********************/
    /*
   1. noCache 不使用缓存，全部走网络

    2. noStore 不使用缓存，也不存储缓存

    3. onlyIfCached 只使用缓存

    4. maxAge 设置最大失效时间，失效则不使用 需要服务器配合

    5. maxStale 设置最大失效时间，失效则不使用 需要服务器配合 感觉这两个类似 还没怎么弄清楚，清楚的同学欢迎留言

    6. minFresh 设置有效时间，依旧如上

    7. FORCE_NETWORK 只走网络

    8. FORCE_CACHE 只走缓存*/


    /**
     * 设缓存有效期为两天
     */
    private const val CACHE_STALE_SEC = 60 * 60 * 24 * 2.toLong()

    /**
     * 查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
     * max-stale 指示客户机可以接收超出超时期间的响应消息。如果指定max-stale消息的值，那么客户机可接收超出超时期指定值之内的响应消息。
     */
    private const val CACHE_CONTROL_CACHE = "only-if-cached, max-stale=$CACHE_STALE_SEC"

    /**
     * 查询网络的Cache-Control设置，头部Cache-Control设为max-age=0
     * (假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，
     * 数据由缓存直接返回)时则不会使用缓存而请求服务器
     */
    private const val CACHE_CONTROL_AGE = "max-age=10"


    //构造方法私有
    private fun getLogInterceptor(): HttpLoggingInterceptor {
        //开启Log
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return logInterceptor
    }

    private val cacheFile: File = File(instance.cacheDir, "cache")
    private val cache = Cache(cacheFile, 1024 * 1024 * 100) //100Mb


    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private val mRewriteCacheControlInterceptor = Interceptor { chain ->
        var request = chain.request()
        val cacheControl = request.cacheControl.toString()
        if (!NetWorkUtils.isNetConnected(instance)) {
            request = request.newBuilder()
                .cacheControl(
                    if (TextUtils.isEmpty(cacheControl))
                        CacheControl.FORCE_NETWORK else CacheControl.FORCE_CACHE
                )
                .build()
        }
        val originalResponse = chain.proceed(request)
        if (NetWorkUtils.isConnected(instance)) {
            originalResponse
                .newBuilder()
                .header("Cache-Control", cacheControl)
                .removeHeader("Pragma")
                .build()
        } else {
            originalResponse
                .newBuilder()
                .header(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=$CACHE_STALE_SEC"
                )
                .removeHeader("Pragma")
                .build()
        }
    }

    //增加头部信息
    val headerInterceptor = Interceptor { chain ->
        val sharedPreferences: SharedPreferences = instance
            .getSharedPreferences(COOKIE, Context.MODE_PRIVATE)
        val string = sharedPreferences.getString(AUTHORIZED, "")
        val split = string!!.split(";").toTypedArray()
        val builder = chain.request().newBuilder()
        if (split.size > 0) {
            builder.addHeader("Cookie", split[0])
        }
        val build = builder
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "*/*")
            .header("Accept-Encoding", "gzip,deflate")
            .build()
        chain.proceed(build)
    }


    val okHttpClient = OkHttpClient.Builder()
        .readTimeout(
            READ_TIME_OUT.toLong(),
            TimeUnit.MILLISECONDS
        )
        .connectTimeout(
            CONNECT_TIME_OUT.toLong(),
            TimeUnit.MILLISECONDS
        )
        .addInterceptor(mRewriteCacheControlInterceptor)
        .addNetworkInterceptor(mRewriteCacheControlInterceptor)
        .addInterceptor(ReceivedCookiesInterceptor())
        /* .addNetworkInterceptor(
             LoggerInteroptor(
                 App.instance.getPackageName(), true
             )
         )*/
        //.addInterceptor(headerInterceptor)
        .addInterceptor(getLogInterceptor())
        .cache(cache)
        .build()


    val gson = GsonBuilder()
        .disableHtmlEscaping()
        .serializeNulls()
        .enableComplexMapKeySerialization()
        .serializeSpecialFloatingPointValues()
        .setLenient() //.addSerializationExclusionStrategy(new IgnoreStrategy())
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ") //.serializeNulls()
        .create()


    var retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        //.addConverterFactory(FastJsonConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl("")
        .build()


    val apiService = retrofit.create(
        ApiService::class.java
    )

    /**
     * 根据网络状况获取缓存的策略 请求时传入
     *
     * @GET
     *  Observable<ResponseBody> getNewsBodyHtmlPhoto(
     *  @Header("Cache-Control") String cacheControl,
     *  @Url String photoPath);
     */
    @NonNull
    fun getCacheControl(): String {
        return if (NetWorkUtils.isNetConnected(instance)) CACHE_CONTROL_AGE else CACHE_CONTROL_CACHE
    }
}


class ReceivedCookiesInterceptor() : Interceptor {
    private var context: Context? = null
    var sharedPreferences: SharedPreferences? = null

    init {
        sharedPreferences = instance.getSharedPreferences(COOKIE, Context.MODE_PRIVATE)
    }

    @SuppressLint("CheckResult")
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse: Response = chain.proceed(chain.request())
        if (!originalResponse.headers("Set-cookie").isEmpty()) {
            val cookieBuffer = StringBuffer()
            val headers = originalResponse.headers("Set-cookie")
            val cookie = SPUtils.getSharedStringData(instance, "cookie")
            if (TextUtils.isEmpty(cookie)) {
                Observable.fromIterable(headers)
                    .map { s: String ->
                        Log.e("Response-cookie", s)
                        val cookieArray = s.split(";".toRegex()).toTypedArray()
                        cookieArray[0]
                    }
                    .subscribe { s: String? ->
                        cookieBuffer.append(s).append(";")
                        SPUtils.setSharedStringData(
                            instance,
                            AUTHORIZED, cookieBuffer.toString()
                        )
                    }
            }
        }
        return originalResponse
    }
}