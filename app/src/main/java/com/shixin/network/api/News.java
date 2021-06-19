package com.shixin.network.api;

import com.shixin.bean.Newsbean;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by admin on 2017/3/3 0003.
 */

public interface News {

    /*
     动态添加请求头
     @Header
    String value：默认为""，参数名称

    @GET("/")
    Call<ResponseBody> query(@Header("Accept-Language") String lang);

    @HeaderMap

    @GET("/search")
    Call<ResponseBody> query(@HeaderMap Ma<String, String> headers);
    */

    /*静态添加
    @Headers("Cache-Control: max-age=640000")
    @GET("/tasks")
    Call<List<Task>> getTasks();

    @Headers({
        "X-Foo: Bar",
        "X-Ping: Pong"
    })
    @GET("/")
    Call(ResponseBody) deleteObject(@Query("id") String id);
    */

    /**
    * OkHttp 请求拦截器
    *     方式一:
    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                    .header("User-Agent", "Your-App-Name")
                    .header("Accept", "application/vnd.yourapi.v1.full+json")
                    .method(original.method(), original.body())
                    .build();

                return chain.proceed(request);
            }
    }

    方式二:
    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                    .addHeader("Cache-Control", "no-cache")
                    .addHeader("Cache-Control", "no-cache")
                    .method(original.method(), original.body())
                    .build();

                return chain.proceed(request);
            }
    }

    OkHttpClient client = httpClient.build();
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build();

    OkHtt请求头通过拦截器添加Header，两种方式的不同
    .header(key, val):如果key相同，最后一个val会将前面的val值覆盖
    .addHeader(key, val):如果key相同，最后一个val不会将前面的val值覆盖，而是新添加一个Header
    *
    * */


    @GET("news/AppHome/newslist/category/1/page/1.html")
    Observable<Newsbean> search();
}
