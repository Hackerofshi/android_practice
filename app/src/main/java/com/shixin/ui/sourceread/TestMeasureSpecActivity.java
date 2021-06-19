package com.shixin.ui.sourceread;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.shixin.R;

public class TestMeasureSpecActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_measurespec);
        ImageView imageView = findViewById(R.id.img);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}
