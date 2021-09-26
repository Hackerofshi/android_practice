package com.shixin.ui.rxjava;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.shixin.R;
import com.shixin.task.MultiAsynctaskNetwork;
import com.shixin.task.NetworkInterface;

import java.util.ArrayList;
import java.util.List;

public class EventActivity extends AppCompatActivity implements NetworkInterface {

    private static final String   TAG = "--------";
    private              TextView tv1;

    List<?> list = new ArrayList<>();

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        tv1 = (TextView) findViewById(R.id.tv1);

        ImageView imageView = findViewById(R.id.img);
        list = new ArrayList<String>();

        Glide.with(this).load("").into(imageView);


        // AsyncTask
        findViewById(R.id.event).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e(TAG, "OnTouch ACTION_DOWN");
                        //return true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.e(TAG, "OnTouch ACTION_MOVE");
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e(TAG, "OnTouch ACTION_UP");
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        findViewById(R.id.bu1).setOnClickListener(v -> {
            MultiAsynctaskNetwork network = new MultiAsynctaskNetwork(EventActivity.this);
            network.execute();
        });
        new Test(this);
    }

    @Override
    public void onResult(String result) {
        tv1.setText(result);
    }

    class Test {
        Test(EventActivity activity) {

        }
    }
}
