package com.shixin.ui.rxjava;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.shixin.R;
import com.shixin.task.MultiAsynctaskNetwork;
import com.shixin.task.NetworkInterface;

import java.util.ArrayList;
import java.util.List;

public class EventActivity extends AppCompatActivity implements NetworkInterface{

    private static final String TAG = "--------";
    private TextView tv1;

    List<?> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        tv1 = (TextView) findViewById(R.id.tv1);

        list = new ArrayList<String>();


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
    }

    @Override
    public void onResult(String result) {
        tv1.setText(result);
    }
}
