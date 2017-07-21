package com.mobile.healthmate.manager.user;

import com.mobile.healthmate.app.lib.json.JsonField;

/**
 * 登录信息
 * Created by fujl-mac on 2017/7/11.
 */

public class LoginInfo {
    @JsonField("userId")
    private String userId = "";

    @JsonField("username")
    private String username = "";

    @JsonField("password")
    private String password = "";

    @JsonField("accessToken")
    private String accessToken = "";

    @JsonField("loginDt")
    private double loginDt;

    @JsonField("createTime")
    private String createTime = "";

    @JsonField("optLock")
    private int optLock;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public double getLoginDt() {
        return loginDt;
    }

    public void setLoginDt(double loginDt) {
        this.loginDt = loginDt;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getOptLock() {
        return optLock;
    }

    public void setOptLock(int optLock) {
        this.optLock = optLock;
    }

    @Override
    public String toString() {
        return "LoginInfo{" +
                "userId=" + userId +
                ", username=" + username +
                ", password=" + password +
                ", accessToken='" + accessToken + '\'' +
                ", loginDt='" + loginDt + '\'' +
                ", createTime='" + createTime + '\'' +
                ", optLock='" + optLock + '\'' +
                '}';
    }
}
