package com.example.suyobe.interceptcaller;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Method;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_ipnumber;
    private SharedPreferences sp;
    private Button outcall;
    private Button incall;
    private Context mContext;
    final public static int REQUEST_CODE_ASK_CALL_PHONE =123;
    final public static int REQUEST_CODE_ASK_INCALL_PHONE =456;
    final public static int REQUEST_CODE_ASK_RECALL_PHONE =789;
    public NetworkChangeReceiver mNetworkChangeReceiver;
    private TelephonyManager tm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_ipnumber = (EditText) findViewById(R.id.et_ipnumber);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        outcall = (Button)findViewById(R.id.et_outcall);
        incall = (Button)findViewById(R.id.et_incall);
        outcall.setOnClickListener(this);
        incall.setOnClickListener(this);
        this.mContext = this;

        registerBroadcast();


    }

    /**
     * 申请权限
     */
    private void requestPermission() {
        //判断Android版本是否大于23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkoutCallPhonePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.PROCESS_OUTGOING_CALLS);
            int checkinCallPhonePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE);
            int checkinreallPhonePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE);

            if ( (checkoutCallPhonePermission!= PackageManager.PERMISSION_GRANTED)||( checkinCallPhonePermission!= PackageManager.PERMISSION_GRANTED)||
                    ( checkinreallPhonePermission!= PackageManager.PERMISSION_GRANTED)){
                    //请求权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS,Manifest.permission.CALL_PHONE,
                                Manifest.permission.READ_PHONE_STATE},
                        REQUEST_CODE_ASK_CALL_PHONE);
            }
        }else {
                //do something
            SavingNunber();
        }
    }


    /**
     * 注册权限申请回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CODE_ASK_CALL_PHONE){
            for (int i = 0; i < grantResults.length; i++){
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
                {
                    //do something
                    SavingNunber();
                }else
                {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void SavingNunber(){
        String number = et_ipnumber.getText().toString().trim();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("number", number);
        editor.commit();
    }



    //广播获取网络连接的情况
    private void registerBroadcast() {
        mNetworkChangeReceiver = new NetworkChangeReceiver(new NetworkChangeReceiver.NetWorkListener() {
            @Override
            public void networkConnect(boolean b) {
                if (!b) {//没有网络
                    //do something
                } else {//连接网络
                    //do something
                }
            }
        });
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        registerReceiver(mNetworkChangeReceiver, filter);
    }
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.et_outcall:
                requestPermission();
                SavingNunber();
                Toast.makeText(this, "已拦截拨出", Toast.LENGTH_SHORT).show();
            case R.id.et_incall:
                requestPermission();
                SavingNunber();
                Toast.makeText(this, "已拦截拨入", Toast.LENGTH_SHORT).show();
        }
    }
}
