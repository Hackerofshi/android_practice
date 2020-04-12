package com.shixin.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.shixin.basex.RxManager;

public class BaseActivity extends AppCompatActivity {
    public RxManager mRxManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRxManager = new RxManager();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRxManager.clear();


    }
}
