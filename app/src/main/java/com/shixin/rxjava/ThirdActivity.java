package com.shixin.rxjava;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import calc.aidl.ICalcAIDL;

public class ThirdActivity extends AppCompatActivity {

    private ICalcAIDL mcalcAidl;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mcalcAidl = ICalcAIDL.Stub.asInterface(service);
            Log.e("client", "onServiceConnected");

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("client", "onServiceDisconneted");
            mcalcAidl = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
    }

    public void bindService(View view) {
        Intent intent = new Intent();
        intent.setAction("aidl.calc");
        intent.setPackage("com.shixin.rxjava");
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unbindService(View view) {
        unbindService(mServiceConnection);
    }


    /**
     * 点击50-12按钮时调用
     *
     * @param view
     */
    public void minInvoked(View view) throws Exception {

        if (mcalcAidl != null) {
            int addRes = mcalcAidl.min(58, 12);
            Toast.makeText(this, addRes + "", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "服务端未绑定或被异常杀死，请重新绑定服务端", Toast.LENGTH_SHORT)
                    .show();

        }

    }

    /**
     * 点击12+12按钮时调用
     *
     * @param view
     */
    public void addInvoked(View view) throws Exception {

        if (mcalcAidl != null) {
            int addRes = mcalcAidl.add(12, 12);
            Toast.makeText(this, addRes + "", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "服务器被异常杀死，请重新绑定服务端", Toast.LENGTH_SHORT)
                    .show();

        }

    }


}
