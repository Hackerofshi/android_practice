package com.shixin.ui.windowmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.Toast;

import com.shixin.rxjava.R;
import com.shixin.ui.HomeActivity;

public class WindowManagerDemoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window_manager_demo);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(WindowManagerDemoActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent,10);
            }
        }

        WindowManager mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        Button btn = new Button(this);
        btn.setText("btn");
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                0, 0, PixelFormat.TRANSPARENT
        );
        layoutParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE
                | LayoutParams.FLAG_SHOW_WHEN_LOCKED;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        //添加到（100,300） 坐标的位置上
        layoutParams.x = 100;
        layoutParams.y = 300;
        //layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;


        mWindowManager.addView(btn, layoutParams);
        //Flags 参数表示window属性，他有很多选项，通过这些选项可以控制Window的显示特性，以下是比较常用的选项

        //FLAG_NOT_FOCUSABLE 表示window不需要获取焦点，也不需要接收各种输入事件，此标记同时会启用
        // FLAG_NOT_TOUCH_MODAL
        //最终事件会直接传递给下层的具有焦点的window

        //FLAG_NOT_TOUCH_MODAL
        //此模式下，系统将当前window区域以外的单击事件传递给底层的Window,当前Window区域以内的单击事件则
        //自己处理，这个标记很重要，一般来说都需要开启此标记，否则其他Window无法接受到单击事件

        //FLAG_SHOW_WHEN_LOCKED
        //开启此模式可以让Window显示在锁屏的页面上

        //Type 参数表示Window的类型，Window有三种类型，分别是应用Window、子Window和系统的Window。
        //应用类Window对应着一个Activity，子Window不能单独存在，他需要附属在特定的父Window之中，比如常见
        //的一些Dialog就是一个子Window
        //系统Window是需要声明权限在能创建的Window，比如Toast，系统状态栏这些都是系统的Window

        //应用Window的层级是1-99
        //子Window1000-1999
        //系统的Window2000-2999
        //这些层级范围对应着WindowManger.LayoutParams 的type参数。
        //如果想要Window位于所有window的最顶层，那么采用最大层级即可。

    }



}