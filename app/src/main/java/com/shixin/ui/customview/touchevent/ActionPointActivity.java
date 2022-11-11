package com.shixin.ui.customview.touchevent;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.shixin.R;
import com.shixin.ui.view.customview.scroller.TextViewScroller;
import com.shixin.ui.view.customview.touchevent.MoveView;

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

        //onTouch() 与 onTouchEvent()以及click三者的区别和联系 ：
        //
        //onTouch() 与 onTouchEvent() 都是处理触摸事件的 API
        //
        //onTouch() 属于 onTouchListener 接口中的方法，是 View 暴露给用户的接口便于处理触摸事件，
        // 而 onTouchEvent() 是 Android 系统自身对于 Touch 处理的实现
        //
        //先调用 onTouch() 后调用 onTouchEvent()。而且只有当 onTouch() 未消费 Touch 事件才有可能调用到onTouchEvent()。
        // 即 onTouch() 的优先级比 onTouchEvent() 的优先级更高。
        //
        //在 onTouchEvent() 中处理 ACTION_UP 时会利用 ClickListene r执行 Click 事件，所以 Touch 的处理是优先于 Click 的


        mv.setOnTouchListener((v, event) -> false);
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
        findViewById(R.id.rl).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }
}