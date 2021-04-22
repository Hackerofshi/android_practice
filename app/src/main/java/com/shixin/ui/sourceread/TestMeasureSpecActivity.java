package com.shixin.ui.sourceread;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.shixin.rxjava.R;

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
