package com.mobile.healthmate.manager.setting;

/**
 * 设置改变监听器，当应用程序设置发生改变时，该方法会回调
 * Created by zengdexing on 2017/1/9.
 */
public interface OnSettingsChangeListener {
    /**
     * 设置发生改变的回调
     *
     * @param key   设置名称
     * @param value 设置的新值
     */
    public void onSettingsChange(String key, String value);
}
