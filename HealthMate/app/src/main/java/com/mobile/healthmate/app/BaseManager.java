package com.mobile.healthmate.app;

import android.os.Handler;

import com.mobile.healthmate.manager.ResultCode;



/**
 * 管理器基类，所有管理器都必须继承自本类
 * Created by fujl-mac on 2017/7/11.
 */

public abstract class BaseManager implements ResultCode {
    protected Logger logger = new Logger(this);

    /**
     * 管理器被初始化的回调，初始化整个管理器
     */
    public abstract void onManagerCreate(App application);

    /**
     * 所有管理器都初始化后执行
     */
    public void onAllManagerCreated() {

    }

    /**
     * 获得应用程序实例
     *
     * @return 应用实例
     */
    public App getApplication() {
        return App.getInstance();
    }

    /**
     * 获得管理器
     *
     * @param manager 管理器类型
     * @param <M>     管理器Class
     * @return 管理器
     */
    public <M extends BaseManager> M getManager(Class<M> manager) {
        return getApplication().getManager(manager);
    }

    public Handler getHandler() {
        return App.getHandler();
    }
}
