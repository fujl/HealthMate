package com.mobile.healthmate.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.mobile.healthmate.BuildConfig;
import com.mobile.healthmate.app.lib.imageloader.ImageLoader;
import com.mobile.healthmate.manager.NetworkManager;
import com.mobile.healthmate.manager.SdManager;
import com.mobile.healthmate.manager.user.UserManager;
import com.mobile.healthmate.ui.main.adapter.ExceptionCaughtAdapter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 程序
 * Created by fujl-mac on 2017/7/11.
 */

public class App extends Application {
    private static App application;
    private static Handler handler = new Handler();
    private HashMap<String, BaseManager> managers = new HashMap<String, BaseManager>();

    public static Handler getHandler() {
        return handler;
    }

    public static App getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;
        // 初始化图片加载
        ImageLoader.getInstance().init(this);

        String pidName = getUIPName();
        if (pidName.equals(getPackageName())) {
            // 初始化管理器
            initManager();
        }
        //调试模式下打印崩溃日志
        if (BuildConfig.DEBUG) {
            Thread.UncaughtExceptionHandler handler = Thread.getDefaultUncaughtExceptionHandler();
            ExceptionCaughtAdapter exceptionCaughtAdapter = new ExceptionCaughtAdapter(handler);
            Thread.setDefaultUncaughtExceptionHandler(exceptionCaughtAdapter);
        }
    }

    private String getUIPName() {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return "";
    }

    public <V extends BaseManager> V getManager(Class<V> cls) {
        return (V) managers.get(cls.getName());
    }

    private void initManager() {
        List<BaseManager> managerList = new ArrayList<BaseManager>();
        registerManager(managerList);
        for (BaseManager baseManager : managerList) {
            baseManager.onManagerCreate(this);
            managers.put(baseManager.getClass().getName(), baseManager);
        }

        for (Map.Entry<String, BaseManager> entry : managers.entrySet()) {
            entry.getValue().onAllManagerCreated();
        }
    }

    public void injectManager(Object object) {
        Class<?> aClass = object.getClass();

        while (aClass != BaseManager.class && aClass != Object.class) {
            Field[] declaredFields = aClass.getDeclaredFields();
            if (declaredFields != null && declaredFields.length > 0) {
                for (Field field : declaredFields) {
                    int modifiers = field.getModifiers();
                    if (Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers)) {
                        // 忽略掉static 和 final 修饰的变量
                        continue;
                    }

                    if (!field.isAnnotationPresent(Manager.class)) {
                        continue;
                    }

                    Class<?> type = field.getType();
                    if (!BaseManager.class.isAssignableFrom(type)) {
                        throw new RuntimeException("@Manager 注解只能应用到BaseManager的子类");
                    }

                    BaseManager baseManager = getManager((Class<? extends BaseManager>) type);

                    if (baseManager == null) {
                        throw new RuntimeException(type.getSimpleName() + " 管理类还未初始化！");
                    }

                    if (!field.isAccessible())
                        field.setAccessible(true);

                    try {
                        field.set(object, baseManager);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

            aClass = aClass.getSuperclass();
        }
    }

    /**
     * 注册管理器
     */
    private void registerManager(List<BaseManager> lists) {
        lists.add(new NetworkManager());            // 网络管理器
        lists.add(new UserManager());               // 用户管理

        lists.add(new SdManager());                 // 路径管理类
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    public @interface Manager {

    }
}
