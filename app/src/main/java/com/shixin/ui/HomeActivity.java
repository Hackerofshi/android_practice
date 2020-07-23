package com.shixin.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.shixin.rxjava.R;
import com.shixin.ui.customview.RVActivity;
import com.shixin.ui.sourceread.LLActivity;
import com.shixin.ui.sourceread.RLActivity;
import com.shixin.ui.sourceread.ReadSourceActivity;
import com.shixin.ui.sourceread.VpActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ReadSourceActivity.class));
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, RVActivity.class));
            }
        });
    }
}
