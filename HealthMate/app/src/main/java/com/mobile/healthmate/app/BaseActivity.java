package com.mobile.healthmate.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.mobile.healthmate.manager.ResultCode;
import com.mobile.healthmate.manager.ScreenStateManager;
import com.mobile.healthmate.manager.user.UserManager;
import com.mobile.healthmate.view.dialog.LoadingProgressDialog;

import java.util.List;

/**
 * 程序基本类，所有的
 * Created by fujl-mac on 2017/7/11.
 */

public class BaseActivity extends AppCompatActivity implements ResultCode {
    protected Logger logger = new Logger(BaseActivity.class);
    private Toast mToast;
    private boolean mIsDestroy = false;
    private LoadingProgressDialog mCurrentProgressDialog;
    private boolean isBackGround = false;

    /**
     * DIP 转 PX
     */
    public static int dipToPx(float dip) {
        return dipToPx(App.getInstance(), dip);
    }

    public static int dipToPx(Context context, float dip) {
        return (int) (context.getResources().getDisplayMetrics().density * dip + 0.5f);
    }

    public static int sp2px(float spValue) {
        final float fontScale = App.getInstance().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIsDestroy = false;
        App.getInstance().injectManager(this);
    }

    @Override
    protected void onDestroy() {
        mIsDestroy = true;
        super.onDestroy();
    }

    @Deprecated
    @Override
    public View findViewById(@IdRes int id) {
        return super.findViewById(id);
    }

    public void onBackClick(View view) {
        finish();
    }

    public Activity getActivity() {
        return this;
    }

    /**
     * 获得管理器
     *
     * @param manager 管理器类型
     * @param <M>     管理器Class
     * @return 管理器
     */
    public <M extends BaseManager> M getManager(Class<M> manager) {
        return App.getInstance().getManager(manager);
    }

    public void startActivity(Class<? extends BaseActivity> cls) {
        startActivity(new Intent(this, cls));
    }

    public void showToast(@StringRes int resId) {
        showToast(getString(resId));
    }

    public void showToast(@NonNull String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public void showLoadingProgressDialog() {
        showLoadingProgressDialog(false);
    }

    public void showLoadingProgressDialog(boolean isCancelable) {
        if (isDestroy()) {
            return;
        }

        if (mCurrentProgressDialog == null) {
            mCurrentProgressDialog = new LoadingProgressDialog(this);
        }
        mCurrentProgressDialog.setCancelable(isCancelable);
        if (!mCurrentProgressDialog.isShowing()) {
            try {
                mCurrentProgressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void dismissLoadingProgressDialog() {
        if (mCurrentProgressDialog != null) {
            try {
                if (mCurrentProgressDialog.isShowing()) {
                    mCurrentProgressDialog.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return isDestroyed();
        }
        return mIsDestroy;
    }

    /**
     * 隐藏键盘
     */
    public void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View currentFocus = this.getCurrentFocus();
        if (currentFocus == null)
            return;
        IBinder windowToken = currentFocus.getWindowToken();
        imm.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 打开键盘
     *
     * @param view view
     */
    public void openSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getManager(UserManager.class).getCurrentUid().isEmpty()) {
            return;
        }
        if (!isNeedOnResume()) {
            isBackGround = false;
            getManager(ScreenStateManager.class).setScreenOff(false);
            return;
        }
        if (isBackGround || getManager(ScreenStateManager.class).isScreenOff()) {
            isBackGround = false;
            getManager(ScreenStateManager.class).setScreenOff(false);
//            checkLastEnterTime(false); //4.0.4需求：在应用程序启动需输入手势密码，从后台回到程序，无需输入手势密码
        }
//        getManager(MsgManager.class).getMsgNotification().notificationEnable = false;
//        getManager(MsgManager.class).getMsgNotification().cancelMessageNotification();
    }

    protected void checkLastEnterTime(boolean isNeedToHome) {

//        String lock = getManager(SettingManager.class).getSetting(SettingKey.KEY_GESTURE_LOCK.getValue(), null);
//        if (lock == null) {
//            startActivity(new Intent(getActivity(), LockSetupActivity.class));
//        } else {
//            LockActivity.startActivity(this, isNeedToHome);
//        }

    }

    protected boolean isNeedOnResume() {
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isAppBackGround()) {
            isBackGround = true;
//            getManager(SettingManager.class).putSetting(SettingKey.KEY_ENTER_BACKGROUND_TIME.getValue(), String.valueOf(System.currentTimeMillis()));
        }
    }

    //判断程序是否在前台运行
    public boolean isAppBackGround() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND
                        || appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE
                        || appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE) {
                    return true;
                } else {
                    return false;
                }

            }
        }

        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
//        getManager(MsgManager.class).getMsgNotification().notificationEnable = true;
    }
}
