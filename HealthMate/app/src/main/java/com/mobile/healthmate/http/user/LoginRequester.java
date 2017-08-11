package com.mobile.healthmate.http.user;

import android.support.annotation.NonNull;

import com.mobile.healthmate.app.lib.json.JsonHelper;
import com.mobile.healthmate.http.OnHttpCodeListener;
import com.mobile.healthmate.http.lib.SimpleRequester;
import com.mobile.healthmate.manager.user.LoginInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * 登录请求
 * Created by fujl-mac on 2017/7/17.
 */

public class LoginRequester extends SimpleRequester<LoginInfo> {

    private String username;
    private String password;

    public LoginRequester(String username, String password, OnHttpCodeListener<LoginInfo> onHttpCodeListener) {
        super(onHttpCodeListener);
        this.username = username;
        this.password = password;
    }

    @Override
    public LoginInfo onDumpData(JSONObject jsonObject) throws JSONException {
        JSONObject infoJson = jsonObject.optJSONObject("errData");
        return JsonHelper.toObject(infoJson, LoginInfo.class);
    }

    @Override
    protected void onPutParams(@NonNull Map<String, String> params) {
        params.put("username", username);
        params.put("password", password);
    }

    @NonNull
    @Override
    public String getChildrenUrl() {
        return "api/login.do";
    }
}
