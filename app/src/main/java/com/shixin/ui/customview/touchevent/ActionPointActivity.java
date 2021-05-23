package com.shixin.ui.customview.touchevent;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.shixin.customview.scroller.TextViewScroller;
import com.shixin.customview.touchevent.MoveView;
import com.shixin.rxjava.R;

public class ActionPointActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_point);
        MoveView         mv       = findViewById(R.id.mv);
        TextViewScroller scroller = findViewById(R.id.tvs);
        scroller.setBoundary(0, 200);

        //dispatchTouchEvent(MotionEvent ev)
        //onInterceptTouchEvent(MotionEvent ev)

        //优先级
        //onTouchListener  返回true这拦截事件，下面的方法将不再调用
        //onTouchEvent     返回false,该方法将不再执行
        //onClickListener  调用onTouchEvent时，在某一个手势里面调用点击事件

        /*在onTouchEvent 方法里面，必须在适当的时候，调用performClick方法，如：
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // 如果点击
            performClick();
        }*/


        mv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        mv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("onClick", "onClick: ");
            }
        });
        findViewById(R.id.view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("onClick", "View: ");
            }
        });
    }
}