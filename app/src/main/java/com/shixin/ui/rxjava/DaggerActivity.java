package com.shixin.ui.rxjava;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.shixin.R;

public class DaggerActivity extends AppCompatActivity {

    /*@Inject
    Car car;

    //添加@Inject注解，表示这个mPoetry是需要注入的
    @Inject
    Poetry mPoetry;

    @Inject
    Gson mGson;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dagger);
        // 使用Dagger2生成的类 生成组件进行构造，并注入
        // 使用Dagger2生成的类 生成组件进行构造，并注入
        /*MainComponent.getInstance()
                .inject(this);*/
       /* MainComponent.getInstance().inject(this);
        System.out.println(car.test());

        System.out.println("-----" + mGson.toJson(mPoetry) + mPoetry);*/
    }
}
