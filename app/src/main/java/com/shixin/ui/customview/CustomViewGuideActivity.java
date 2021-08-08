package com.shixin.ui.customview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.shixin.R;
import com.shixin.ui.customview.touchevent.ActionPointActivity;

public class CustomViewGuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view_guide);
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomViewGuideActivity.this, RVActivity.class));
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomViewGuideActivity.this, RVCoverActivity.class));
            }
        });
        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomViewGuideActivity.this, RV1Activity.class));
            }
        });

        findViewById(R.id.btn4).setOnClickListener(v ->
                startActivity(new Intent(CustomViewGuideActivity.this, ActionPointActivity.class)));

        findViewById(R.id.btn5).setOnClickListener(v ->
                startActivity(new Intent(CustomViewGuideActivity.this, CustomDragScaleActivity.class)));
    }
}