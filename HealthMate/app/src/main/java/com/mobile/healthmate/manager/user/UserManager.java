package com.mobile.healthmate.manager.user;

import android.content.SharedPreferences;

import com.mobile.healthmate.BuildConfig;
import com.mobile.healthmate.app.App;
import com.mobile.healthmate.app.BaseManager;
import com.mobile.healthmate.app.lib.json.JsonHelper;
import com.mobile.healthmate.http.OnHttpCodeListener;
import com.mobile.healthmate.http.user.LoginRequester;
import com.mobile.healthmate.manager.OnResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户管理器
 * Created by fujl-mac on 2017/7/11.
 */

public class UserManager extends BaseManager {
    private static final String SHARED_PREFERENCES_NAME = "user_info";
    private LoginInfo mLoginInfo;
    private List<OnUserChangeListener> onUserChangeListeners = new ArrayList<>();

    @Override
    public void onManagerCreate(App application) {
        loadUser(application);
    }

    @Override
    public void onAllManagerCreated() {
        super.onAllManagerCreated();
    }

    /**
     * 判断用户是否登录
     *
     * @return true：已登录， false：未登录
     */
    public boolean isLogin() {
        return !mLoginInfo.getUserId().isEmpty();
    }

    /**
     * 添加用户变化监听器，监听用户登录、登出、属性的变化
     * <p>适当的时候需要调用removeOnUserChangeListener方法，防止内存泄漏</p>
     *
     * @param onUserChangeListener 用户监听
     */
    public void addOnUserChangeListener(OnUserChangeListener onUserChangeListener) {
        onUserChangeListeners.add(onUserChangeListener);
    }

    /**
     * 注销用户变化监听器
     *
     * @param onUserChangeListener 用户监听
     */
    public void removeOnUserChangeListener(OnUserChangeListener onUserChangeListener) {
        onUserChangeListeners.remove(onUserChangeListener);
    }

    public String getCurrentUid() {
        return getLoginInfo().getUserId();
    }

    /**
     * 返回用户信息
     *
     * @return 用户
     */
    public LoginInfo getLoginInfo() {
        assert mLoginInfo != null : "程序运行错误， userInfo == null";
        return JsonHelper.toObject(JsonHelper.toJSONObject(mLoginInfo), LoginInfo.class);
    }

    // 持久化用户信息
    private void saveUserInfo() {
        if (BuildConfig.DEBUG) {
            logger.i("保存用户信息：%s", mLoginInfo);
        }

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(SHARED_PREFERENCES_NAME, 0);
        JSONObject userJson = JsonHelper.toJSONObject(mLoginInfo);
        sharedPreferences.edit().putString("user", userJson.toString()).apply();
    }

    /**
     * 加载用户信息
     */
    private void loadUser(App hcApplication) {
        SharedPreferences sharedPreferences = hcApplication.getSharedPreferences(SHARED_PREFERENCES_NAME, 0);
        String userString = sharedPreferences.getString("user", null);
        if (userString == null) {
            mLoginInfo = new LoginInfo();
        } else {
            try {
                mLoginInfo = JsonHelper.toObject(new JSONObject(userString), LoginInfo.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (mLoginInfo == null) {
                if (BuildConfig.DEBUG) {
                    throw new RuntimeException("用户信息保存错误！！！！");
                } else {
                    mLoginInfo = new LoginInfo();
                }
            }
        }
        if (BuildConfig.DEBUG) {
            logger.i("启动，加载用户信息：%s", mLoginInfo);
        }
    }

    /**
     * 登录
     * 注意：本方法不能多次调用，正在登录或者已经登录后不可再调用
     *
     * @param username              账号
     * @param password              密码
     * @param onResultListener      结果监听
     */
    public void login(final String username, final String password, final OnResultListener onResultListener) {
        LoginRequester requester = new LoginRequester(username, password, new OnHttpCodeListener<LoginInfo>() {
            @Override
            public void onHttpResponse(int code, LoginInfo loginInfo) {
                if (code == RESULT_CODE_OK) {
                    mLoginInfo = loginInfo;
                    saveUserInfo();
                    notifyUserWillLogin();
                    notifyUserDidLogin();
                }
                onResultListener.onResult(code);
            }
        } );
        requester.doPost();
    }

    private void notifyUserWillLogin() {
        for (OnUserChangeListener onUserChangeListener : onUserChangeListeners) {
            onUserChangeListener.onUserWillLogin(mLoginInfo);
        }
    }

    private void notifyUserDidLogin() {
        for (OnUserChangeListener onUserChangeListener : onUserChangeListeners) {
            onUserChangeListener.onUserDidLogin(mLoginInfo);
        }
    }
}
