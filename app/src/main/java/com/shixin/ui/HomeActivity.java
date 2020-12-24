package com.shixin.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.hi.dhl.paging3.network.ui.MainPagingActivity;
import com.shixin.jetpack.databinding.MainActivity;
import com.shixin.rxjava.R;
import com.shixin.ui.customview.RVActivity;
import com.shixin.ui.ndktest.NdkTestActivity;
import com.shixin.ui.sourceread.ReadSourceActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViewById(R.id.btn1).setOnClickListener(view ->
                startActivity(new Intent(HomeActivity.this, ReadSourceActivity.class)));

        findViewById(R.id.btn2).setOnClickListener(view ->
                startActivity(new Intent(HomeActivity.this, RVActivity.class)));

        findViewById(R.id.btn_databinding).setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
        });

        findViewById(R.id.btn_Paging).setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, MainPagingActivity.class));
        });

        findViewById(R.id.btn_ndk).setOnClickListener(v->{
            startActivity(new Intent(HomeActivity.this, NdkTestActivity.class));
        });
    }
}
