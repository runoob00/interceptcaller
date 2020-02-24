package com.example.suyobe.interceptcaller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public class OutCallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String outcall = getResultData();
        SharedPreferences sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        String number = sp.getString("number","");
        if (outcall.equals(number)){
            setResultData(null);
        }
    }
}
