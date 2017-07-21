package com.mobile.healthmate.app;

import android.util.Log;

import com.mobile.healthmate.BuildConfig;

/**
 * 日志打印
 * Created by fujl-mac on 2017/7/11.
 */

public class Logger {
    private String tag;

    public Logger(String tag) {
        super();
        this.tag = tag;
    }

    public Logger(Object object) {
        this(object.getClass());
    }

    public Logger(Class<?> clazz) {
        this(clazz.getSimpleName());
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void d(String format, Object... args) {
        if (BuildConfig.DEBUG) {
            String msg = String.format(format, args);
            Log.d(tag, msg);
        }
    }

    public void e(String format, Object... args) {
        if (BuildConfig.DEBUG) {
            String msg = String.format(format, args);
            Log.e(tag, msg);
        }
    }

    public void i(String format, Object... args) {
        if (BuildConfig.DEBUG) {
            String msg = String.format(format, args);
            Log.i(tag, msg);
        }
    }

    public void w(String format, Object... args) {
        if (BuildConfig.DEBUG) {
            String msg = String.format(format, args);
            Log.w(tag, msg);
        }
    }

    public interface OnNewLogListener {
        void onNewLog(LogInfo logInfo);
    }

    public static class LogInfo {
        public long time;
        public String tag;
        public String msg;
        public int level;

        public LogInfo(int level, String tag, String msg) {
            this.tag = tag;
            this.msg = msg;
            this.level = level;
            time = System.currentTimeMillis();
        }
    }
}
