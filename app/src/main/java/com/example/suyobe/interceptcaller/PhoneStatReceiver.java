package com.example.suyobe.interceptcaller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.telephony.ITelephony;
import java.lang.reflect.Method;

public class PhoneStatReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
         if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
         //去电，可以用定时挂断
         } else {
             //由于android没有来点广播所以，去掉拨打电话就是来电状态了
             Log.e("msg", "coming");
             String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
             Log.e("msg", "State: "+ state);
             String inNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
             Log.e("msg", "Incomng Number: " + inNumber);
             SharedPreferences sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
             String number = sp.getString("number","");
             if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)) {
                if(inNumber.equals(number)){//拦截指定的电话号码
                  try {
                     //挂断电话 方法一
                     Method method = Class.forName(
                     "android.os.ServiceManager").getMethod(
                     "getService", String.class);
                     // 获取远程TELEPHONY_SERVICE的IBinder对象的代理
                     IBinder binder = (IBinder) method.invoke(null,
                     new Object[] { Context.TELEPHONY_SERVICE });
                     // 将IBinder对象的代理转换为ITelephony对象
                     ITelephony telephony = ITelephony.Stub.asInterface(binder);
                     // 挂断电话
                     telephony.endCall();
                     Log.e("msg", "end");


//                     //挂断电话 方法二
//                     ITelephony iTelephony = getITelephony(context); //获取电话接口
//                     iTelephony.endCall(); // 挂断电话
//                     Log.e("msg", "end");


                  } catch (Exception e) {
                            e.printStackTrace();
                         Log.e("msg", "cuol a as ");
                  }
                }
             }
         }
    }
    private static ITelephony getITelephony(Context context) {
        ITelephony iTelephony=null;
        TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Class<TelephonyManager> c = TelephonyManager.class;
        Method getITelephonyMethod = null;
        try {
            getITelephonyMethod = c.getDeclaredMethod("getITelephony", (Class[]) null); // 获取声明的方法
            getITelephonyMethod.setAccessible(true);
            } catch (SecurityException e) {
                 e.printStackTrace();
            } catch (NoSuchMethodException e) {
                 e.printStackTrace();
            }
        try {
             iTelephony = (ITelephony) getITelephonyMethod.invoke(
             mTelephonyManager, (Object[]) null); // 获取实例
             return iTelephony;
        } catch (Exception e) {
             e.printStackTrace();
        }
             return iTelephony;
    }
}