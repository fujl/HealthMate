package com.mobile.healthmate.manager.setting;

import android.content.SharedPreferences;

import com.mobile.healthmate.app.App;
import com.mobile.healthmate.app.BaseManager;
import com.mobile.healthmate.manager.user.OnUserChangeListener;
import com.mobile.healthmate.manager.user.LoginInfo;
import com.mobile.healthmate.manager.user.UserManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置 保存
 * Created by zengdexing on 2017/1/9.
 */
public class SettingManager extends BaseManager {

    public static final String SYN_MSG_ID = "syn_msg_id";

    public static String VALUE_TRUE = "true";
    public static String VALUE_FALSE = "false";

    /**
     * 用户的配置文件， 当切换用户时， 该配置会重新加载
     */
    private SharedPreferences sharedPreferences;

    /**
     * 整个应用程序配置
     */
    private SharedPreferences applicationPreferences;

    @App.Manager
    private UserManager userManager;

    private List<OnSettingsChangeListener> onSettingsChangeListeners = new ArrayList<>();
    private OnUserChangeListener onUserChangeListener = new OnUserChangeListener.SimpleOnUserChangeListener() {
        @Override
        public void onUserDidLogin(LoginInfo userInfo) {
            initUserSetting();
        }

        @Override
        public void onUserDidLogout(LoginInfo userInfo) {
            initUserSetting();
        }
    };

    @Override
    public void onManagerCreate(App application) {
        applicationPreferences = application.getSharedPreferences("application_settings", 0);

        initUserSetting();

        userManager.addOnUserChangeListener(onUserChangeListener);
    }

    private void initUserSetting() {
        sharedPreferences = getApplication().getSharedPreferences("settings_" + userManager.getCurrentUid(), 0);
    }

    /**
     * 保存设置，注意，该方法只会保存到当前用户的配置文件，不同的用户配置会不同， 注意和
     * {@link #putApplicationSetting(String, String)} 方法的区别。
     * <p>
     * 本方法用于保存一些用户设置、配置信息，不建议保存用户数据， 否则造成文件增大拖慢访问速度
     *
     * @param key   配置名称
     * @param value 配置的值
     */
    public void putSetting(String key, String value) {
        putSetting(key, value, true);
    }

    /**
     * 保存用户配置
     *
     * @param isNotify 是否通知刷新界面
     * @see #putSetting(String, String)
     */
    public void putSetting(String key, String value, boolean isNotify) {
        sharedPreferences.edit().putString(key, value).apply();
        if (isNotify)
            notifyOnSettingsChange(key, value);
    }

    /**
     * 获取当前用户的设置
     *
     * @param key      配置名称
     * @param defValue 如果没有该配置，返回的默认值
     */
    public String getSetting(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    /**
     * 保存应用程序配置，全局配置，所有用户通用配置，如：设备信息，最新版本等。。。如果该配置属于用户，请使用
     * {@link #putSetting(String, String)}方法。
     *
     * @param key   配置名称
     * @param value 配置的值
     */
    public void putApplicationSetting(String key, String value) {
        applicationPreferences.edit().putString(key, value).apply();
        notifyOnSettingsChange(key, value);
    }

    /**
     * 获取应用程序的配置
     *
     * @param key      配置名称
     * @param defValue 如果没有该配置，返回的默认值
     */
    public String getApplicationSetting(String key, String defValue) {
        return applicationPreferences.getString(key, defValue);
    }

    /**
     * 注册设置变化监听器，监听设置变化，监听范围：{@link #putApplicationSetting(String, String)}
     * {@link #putSetting(String, String)}
     * <p>
     * 调用该方法后，不用时应当调用
     * {@link #removeOnSettingsChangeListener(OnSettingsChangeListener)}方法注销
     */
    public void addOnSettingsChangeListener(OnSettingsChangeListener onSettingsChangeListener) {
        onSettingsChangeListeners.add(onSettingsChangeListener);
    }

    private void notifyOnSettingsChange(String settingsKey, String value) {
        for (OnSettingsChangeListener onSettingsChangeListener : onSettingsChangeListeners) {
            onSettingsChangeListener.onSettingsChange(settingsKey, value);
        }
    }

    /**
     * 注销设置变化监听，与
     * {@link #addOnSettingsChangeListener(OnSettingsChangeListener)}配套使用
     */
    public void removeOnSettingsChangeListener(OnSettingsChangeListener onSettingsChangeListener) {
        onSettingsChangeListeners.remove(onSettingsChangeListener);
    }
}
