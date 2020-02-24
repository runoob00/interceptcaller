package com.example.suyobe.interceptcaller;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

class NetworkChangeReceiver extends BroadcastReceiver {

    private NetWorkListener mNetWorkListener;
    //定义个回调接口
    public interface NetWorkListener {
        public void networkConnect(boolean b);
    }
    public  NetworkChangeReceiver(NetWorkListener netWorkListener) {
        mNetWorkListener = netWorkListener;
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        // 监听网络连接，包括wifi和移动数据的打开和关闭
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
            if (activeNetwork != null) { // 连接上网络
                if (activeNetwork.isConnected()) {
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        Toast.makeText(context, "当前WiFi连接可用", Toast.LENGTH_SHORT).show();
                        Log.i("NetworkChangeReceiver", "当前WiFi连接可用 ");
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                    {
                        Toast.makeText(context, "当前移动网络连接可用", Toast.LENGTH_SHORT).show();
                        Log.i("NetworkChangeReceiver", "当前移动网络连接可用 ");
                    }
                    mNetWorkListener.networkConnect(true);
                } else {
                    Toast.makeText(context, "当前没有网络连接，请确保你已经打开网络", Toast.LENGTH_SHORT).show();
                    Log.i("NetworkChangeReceiver", "当前没有网络连接，请确保你已经打开网络 ");
                    mNetWorkListener.networkConnect(false);
                }

            } else {   // 没有连接上
                Toast.makeText(context, "当前没有网络连接，请确保你已经打开网络", Toast.LENGTH_SHORT).show();
                Log.i("NetworkChangeReceiver", "当前没有网络连接，请确保你已经打开网络 ");
                mNetWorkListener.networkConnect(false);
            }
        }
    }
}
