package com.shixin.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;

import javax.inject.Inject;

import com.shixin.dagger.Car;
import com.shixin.dagger.Poetry;
import com.shixin.dagger.component.DaggerMainComponent;

public class Dagger1Activity extends AppCompatActivity {

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
        DaggerMainComponent.builder()
                .build()
                .inject(this);
        System.out.println(car.test());

        System.out.println(mGson.toJson(mPoetry));
    }
}
