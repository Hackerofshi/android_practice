package com.shixin.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;

import javax.inject.Inject;

import dagger.Car;
import dagger.Poetry;
import dagger.component.MainComponent;

public class DaggerActivity extends AppCompatActivity {

    @Inject
    Car car;

    //添加@Inject注解，表示这个mPoetry是需要注入的
    @Inject
    Poetry mPoetry;

    @Inject
    Gson mGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dagger);
        // 使用Dagger2生成的类 生成组件进行构造，并注入
        // 使用Dagger2生成的类 生成组件进行构造，并注入
        /*MainComponent.getInstance()
                .inject(this);*/
        MainComponent.getInstance().inject(this);
        System.out.println(car.test());

        System.out.println("-----" + mGson.toJson(mPoetry) + mPoetry);
    }
}
