package com.shixin.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.shixin.databinding.DatabingTestActivity;
import com.shixin.databinding.MainActivity;
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

        findViewById(R.id.btn_databinding).setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
        });
    }
}
