package com.shixin.ui.ndktest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.shixin.jni.DynamicBridge;
import com.shixin.R;

public class NdkTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ndk_test);
    }
}