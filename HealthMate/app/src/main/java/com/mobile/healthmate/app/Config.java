package com.mobile.healthmate.app;

/**
 * 系统配置
 * Created by fujl-mac on 2017/7/11.
 */

public class Config {
    // 服务
    public static final Server server = Server.TEST;

    public static final String serverUrl;

    static {
        switch (server) {
            case NORMAL:
                serverUrl = "";
                break;
            case TEST:
                serverUrl = "http://m.xygs.gov.cn:8088/";
                break;
            default:
                throw new RuntimeException("请配置服务器地址");
        }
    }

    public enum Server {
        NORMAL,
        TEST,
    }
}
