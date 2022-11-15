package com.shixin.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.LogUtils;
import com.hi.dhl.paging3.network.ui.MainPagingActivity;
import com.shixin.R;
import com.shixin.apt_annotation.AptAnnotation;
import com.shixin.bean.Bird;
import com.shixin.ui.activityrecord.TestActivitySingleTaskActvity;
import com.shixin.ui.customview.CustomViewGuideActivity;
import com.shixin.ui.jetpack.JetIndexActivity;
import com.shixin.ui.jetpack.databinding.MainActivity;
import com.shixin.ui.ndktest.NdkTestActivity;
import com.shixin.ui.practice.PracticeGuideActivity;
import com.shixin.ui.sourceread.ReadSourceActivity;
import com.shixin.ui.sourceread.TestMeasureSpecActivity;
import com.shixin.ui.windowmanager.WindowManagerDemoActivity;
import com.shixin.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

@AptAnnotation(desc = "我是 MainActivity 上面的注解")
public class HomeActivity extends AppCompatActivity {

    @AptAnnotation(desc = "我是 onCreate 上面的注解")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bird bird = new Bird();
        bird.create();

        Log.i("TAG", "onCreate:------- ");

        findViewById(R.id.btn0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, JetIndexActivity.class));
            }
        });

        findViewById(R.id.btn1).setOnClickListener(view ->
                startActivity(new Intent(HomeActivity.this, ReadSourceActivity.class)));

        findViewById(R.id.btn2).setOnClickListener(view ->
                startActivity(new Intent(HomeActivity.this, CustomViewGuideActivity.class)));

        findViewById(R.id.btn_databinding).setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
        });

        findViewById(R.id.btn_Paging).setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, MainPagingActivity.class));
        });

        findViewById(R.id.btn_ndk).setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, NdkTestActivity.class));
        });
        findViewById(R.id.btn_constraint).setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, PracticeGuideActivity.class));
        });
        findViewById(R.id.ll).setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, TestMeasureSpecActivity.class));
        });
        findViewById(R.id.windowmanager).setOnClickListener(v -> {
            addOverlay();
        });
        findViewById(R.id.opengl).setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, TestActivitySingleTaskActvity.class));
        });

        findViewById(R.id.rxjava).setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, com.shixin.ui.rxjava.MainActivity.class));
        });

        testGrid();


        //AsyncTask
    }

    private void testGrid() {
        CommonUtils.Point point = new CommonUtils.Point();
        point.x = 2;
        point.y = 2;

        CommonUtils.Point point1 = new CommonUtils.Point();
        point1.x = 0;
        point1.y = 0;

        CommonUtils.Point point2 = new CommonUtils.Point();
        point2.x = 1;
        point2.y = 0;

        CommonUtils.Point point3 = new CommonUtils.Point();
        point3.x = 1;
        point3.y = 1;

        CommonUtils.Point point4 = new CommonUtils.Point();
        point4.x = 0;
        point4.y = 1;

        List<CommonUtils.Point> pts = new ArrayList<>();
        pts.add(point1);
        pts.add(point2);
        pts.add(point3);
        pts.add(point4);


        boolean inPolygon = CommonUtils.isInPolygon(point, pts);
        LogUtils.i("是否在范围内=" + inPolygon);
    }

    private boolean askedForOverlayPermission;
    private static final int OVERLAY_PERMISSION_CODE = 0xa1;

    public void addOverlay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                askedForOverlayPermission = true;
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_CODE);
            } else {
                Intent serviceIntent = new Intent(HomeActivity.this, WindowManagerDemoActivity.class);
                startActivity(serviceIntent);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_CODE) {
            askedForOverlayPermission = false;
            if (Settings.canDrawOverlays(this)) {
                // SYSTEM_ALERT_WINDOW permission not granted...
                //Toast.makeText(MyProtector.getContext(), "ACTION_MANAGE_OVERLAY_PERMISSION Permission Granted", Toast.LENGTH_SHORT).show();
                Intent serviceIntent = new Intent(HomeActivity.this, WindowManagerDemoActivity.class);
                startService(serviceIntent);
            } else {
                Toast.makeText(getApplicationContext(),
                        "ACTION_MANAGE_OVERLAY_PERMISSION Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
