package com.mobile.healthmate.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mobile.healthmate.app.App;
import com.mobile.healthmate.app.BaseManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 网络变化管理器，负责管理网络变化的通知，如果需要知道当前手机的网络连接类型，调用getNetWorkType函数
 * 如果需要时刻监听网络的断开与连接，请调用：addOnNetworkChangeListener
 * <p>Created by fujl on 2017-07-24.</p>
 */
public class NetworkManager extends BaseManager {
    /**
     * 上一次的网络连接类型
     */
    private int lastConnectType = -1;
    private List<OnNetworkChangeListener> onNetworkChangeListeners = new ArrayList<>();
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            int currentType = -1;
            if (networkInfo != null && networkInfo.isConnected()) {
                currentType = networkInfo.getType();
            } else {
                currentType = -1;
            }


            if (lastConnectType != currentType) {
                lastConnectType = currentType;
                for (OnNetworkChangeListener onNetworkChangeListener : onNetworkChangeListeners) {
                    onNetworkChangeListener.onNetworkChange(currentType);
                }
            }
        }
    };

    @Override
    public void onManagerCreate(App application) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        application.registerReceiver(broadcastReceiver, intentFilter);
    }

    /**
     * 获取当前的网络连接类型 {@link NetworkInfo#getType()}
     * 注意：手机网络并不是只有wifi和移动流量。
     *
     * @return 网络类型包括：wifi，移动数据，蓝牙共享，网线，VPN等等，
     */
    public int getNetWorkType() {
        return lastConnectType;
    }

    /**
     * 添加网络变化监听器
     */
    public void addOnNetworkChangeListener(OnNetworkChangeListener onNetworkChangeListener) {
        onNetworkChangeListeners.add(onNetworkChangeListener);
    }

    /**
     * 移除网络变化监听器
     */
    public void removeOnNetworkChangeListener(OnNetworkChangeListener onNetworkChangeListener) {
        onNetworkChangeListeners.remove(onNetworkChangeListener);
    }

    public interface OnNetworkChangeListener {
        /**
         * 网络变化的回调
         */
        void onNetworkChange(int networkType);
    }

    /**
     * 是否有网
     * @return  true 为有网
     */
    public boolean isNetConect() {
        return lastConnectType != -1;
    }
}
