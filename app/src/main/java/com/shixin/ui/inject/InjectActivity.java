package com.shixin.ui.inject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.shixin.R;
import com.shixin.inject.InjectManager;
import com.shixin.inject.annotation.InjectLayout;
import com.shixin.inject.annotation.InjectView;


@InjectLayout(R.layout.activity_inject)
public class InjectActivity extends AppCompatActivity {


    @InjectView(R.id.btn)
    public Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InjectManager.inject(this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}