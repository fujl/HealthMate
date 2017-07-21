package com.mobile.healthmate.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.mobile.healthmate.app.App;
import com.mobile.healthmate.app.BaseManager;

/**
 * 锁屏管理类
 * Created by nsh on 16/5/31.
 */
public class ScreenStateManager extends BaseManager {

    private boolean isScreenOff = false;


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        private String action = null;

        @Override
        public void onReceive(Context context, Intent intent) {
            action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                // 开屏
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                // 锁屏
                isScreenOff = true;
                Log.d("nsh", "onReceive: screen off " + isScreenOff());
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                // 解锁
            }
        }
    };

    /**
     * 启动screen状态广播接收器
     */
    private void registerReceiver(App application) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        application.registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public void onManagerCreate(App application) {
        registerReceiver(application);
    }

    public boolean isScreenOff() {
        return isScreenOff;
    }

    public void setScreenOff(boolean screenOff) {
        isScreenOff = screenOff;

    }
}
