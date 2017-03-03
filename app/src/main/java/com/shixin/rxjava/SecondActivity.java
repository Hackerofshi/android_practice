package com.shixin.rxjava;

import android.net.Network;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;

import model.Newsbean;
import network.NetWork;
import network.api.News;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class SecondActivity extends AppCompatActivity {

    private Button bu1;
    private static String BASE_URL = "http://api-php.nashigroup.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        bu1 = (Button) findViewById(R.id.bu1);
        bu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initNet();
            }
        });

    }



    private void initNet() {


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//新的配置
                .baseUrl(BASE_URL)
                .build();
        News service = retrofit.create(News.class);

        //可以将这一步封装一下
        NetWork.getNewsApi().search()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Newsbean, List<Newsbean.DataBean>>() {
                    @Override
                    public List<Newsbean.DataBean> call(Newsbean newsbean) {
                        return newsbean.data;
                    }
                }).subscribe(new Subscriber<List<Newsbean.DataBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Newsbean.DataBean> dataBeen) {
                        System.out.println("------------------" + dataBeen.size());
                    }
                });


    }
}
