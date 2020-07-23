package com.shixin.rxjava;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.jakewharton.rxbinding.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.shixin.basex.RxBus;
import com.shixin.bean.Course;
import com.shixin.model.Newsbean;
import com.shixin.network.NetWork;
import com.shixin.network.api.News;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
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
        RxView.clicks(bu1).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        initNet();
                    }
                });



        findViewById(R.id.bu2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Course content = new Course();
                content.name = "hello";
                //这边发出消息
                /**
                 * {@link MainActivity}
                 */
                RxBus.getInstance().post("---", content);
               // finish();
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
                })
                .flatMap(new Func1<List<Newsbean.DataBean>, Observable<Newsbean.DataBean>>() {
                    @Override
                    public Observable<Newsbean.DataBean> call(List<Newsbean.DataBean> dataBeen) {
                        return Observable.from(dataBeen);
                    }
                })
                //制定第几个
                //.elementAt(5)
                .subscribe(new Subscriber<Newsbean.DataBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Newsbean.DataBean dataBean) {
                        System.out.println("------"+dataBean.category);
                    }
                });


    }
}
