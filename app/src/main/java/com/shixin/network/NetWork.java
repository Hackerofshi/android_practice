package com.shixin.network;

import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.shixin.base.BaseApplication;

import com.shixin.network.api.News;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import com.shixin.utils.NetWorkUtils;

/**
 * 项目名称:RxJava
 * 类描述：网络请求类
 * 创建人：shixin
 * 创建时间：2017/3/3 0003.
 * 修改人：shixin
 * 修改时间：2017/3/4 16
 * 修改备注：添加网络缓存
 * Created by shixin on 2017/3/3 0003.
 */
public class NetWork {

    //读超时长，单位：毫秒
    public static final int READ_TIME_OUT = 7676;
    //连接时长，单位：毫秒
    public static final int CONNECT_TIME_OUT = 7676;

    public NetWork() {
        //开启Log
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //缓存
        File cacheFile = new File(BaseApplication.getAppContext().getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb
        //增加头部信息
        Interceptor headerInterceptor =new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request build = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .build();
                return chain.proceed(build);
            }
        };
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .addInterceptor(mRewriteCacheControlInterceptor)
                .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                .addInterceptor(headerInterceptor)
                .addInterceptor(logInterceptor)
                .cache(cache)
                .build();

        if (newsbean == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            newsbean = retrofit.create(News.class);
        }

    }





    private static News newsbean;
    private static String BASE_URL = "http://api-php.nashigroup.com/";



    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();


    public static News getNewsApi() {
        return newsbean;
    }

    /**
     * 云端响应头拦截器 ，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private  final Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            String cacheControl = request.cacheControl().toString();
            if(!NetWorkUtils.isNetConnected(BaseApplication.getAppContext()))
            {
                request = request.newBuilder()
                        .cacheControl(TextUtils.isEmpty(cacheControl)?
                                CacheControl.FORCE_NETWORK:CacheControl.FORCE_CACHE).build();
            }


            Response originalResponse = chain.proceed(request);
            if (NetWorkUtils.isNetConnected(BaseApplication.getAppContext())) {

                return originalResponse.newBuilder()
                        .header("Cache-Control",cacheControl)
                        .removeHeader("Pragma")
                        .build();

            }else {
                return originalResponse.newBuilder()
                        .header("Cache-Control","Public,Only-if-cached,max-stale="+CACHE_STALE_SEC)
                        .removeHeader("Pragma")
                        .build();


            }

        }
    };


    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;

}
