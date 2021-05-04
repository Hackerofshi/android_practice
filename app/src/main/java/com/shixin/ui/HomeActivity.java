package com.shixin.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.hi.dhl.paging3.network.ui.MainPagingActivity;
import com.shixin.jetpack.databinding.MainActivity;
import com.shixin.rxjava.R;
import com.shixin.ui.constraint.ConstraintActivity;
import com.shixin.ui.customview.CustomViewGuideActivity;
import com.shixin.ui.customview.RVActivity;
import com.shixin.ui.customview.RVCoverActivity;
import com.shixin.ui.ndktest.NdkTestActivity;
import com.shixin.ui.sourceread.ReadSourceActivity;
import com.shixin.ui.sourceread.TestMeasureSpecActivity;
import com.shixin.ui.windowmanager.WindowManagerDemoActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
            startActivity(new Intent(HomeActivity.this, ConstraintActivity.class));
        });
        findViewById(R.id.ll).setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, TestMeasureSpecActivity.class));
        });
        findViewById(R.id.windowmanager).setOnClickListener(v -> {
            addOverlay();
        });

    }

    private              boolean askedForOverlayPermission;
    private static final int     OVERLAY_PERMISSION_CODE = 0xa1;

    public void addOverlay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                askedForOverlayPermission = true;
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_CODE);
            }else {
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
