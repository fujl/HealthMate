package com.mobile.healthmate.manager.setting;

/**
 * 设置缓存KEY
 */
public enum SettingKey {
    KEY_GESTURE_LOCK("key_gesture_lock"),
    KEY_ENTER_BACKGROUND_TIME("key_enter_background_time"), ;
    private String value;

    SettingKey(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
