package com.mobile.healthmate.http.lib;

import android.support.annotation.NonNull;

import com.mobile.healthmate.BuildConfig;
import com.mobile.healthmate.app.App;
import com.mobile.healthmate.app.Config;
import com.mobile.healthmate.app.Logger;
import com.mobile.healthmate.app.lib.http.AsyncHttpClient;
import com.mobile.healthmate.app.lib.http.StringResponseHandler;
import com.mobile.healthmate.manager.NetworkManager;
import com.mobile.healthmate.manager.ResultCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Http请求基类
 * Created by fujl-mac on 2017/7/17.
 */

public abstract class HttpRequester {
    public static final String KEY_DATA_LIST = "data";
    public static final Logger logger = new Logger("WEB");

    private StringResponseHandler handler = new StringResponseHandler() {
        @Override
        public void onResult(int code, String content) {
            if (BuildConfig.DEBUG) {
                String serverContent = content == null ? "" : content.replace("%", "%%");
                String string = "response url = " + HttpRequester.this.getUrl() + "code = " + code + "content = " + serverContent;
                logger.i(string);
            }

            try {
                if (code == HttpURLConnection.HTTP_OK) {
                    JSONObject jsonObject = new JSONObject(content);
                    HttpRequester.this.onResult(jsonObject.optInt("errCode"), jsonObject);
                } else {
                    onError(new IOException("HTTP CODE NOT 200"));
                }
            } catch (JSONException e) {
                onError(e);
            }
        }

        @Override
        public void onError(Exception exception) {
            if (BuildConfig.DEBUG) {
                String string = "response url =  " + HttpRequester.this.getUrl() + "error =" + exception;
                string = string.replace("%", "%%");
                logger.i(string);
            }
            HttpRequester.this.onError(exception);
        }
    };

    /**
     * 发起GET请求
     */
    public void doGet() {
        if (!App.getInstance().getManager(NetworkManager.class).isNetConect()) {
            try {
                this.onResult(ResultCode.RESULT_CODE_NET_ERROR, null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return;
        }
        String url = getUrl();
        Map<String, String> params = getParams();
        if (BuildConfig.DEBUG) {
            String string = "get request, url = " + url + "?params=" + params;
            string = string.replace("%", "%%");
            logger.i(string);
        }

        AsyncHttpClient.get(url, params, handler);
    }

    /**
     * 发起Post请求
     */
    public void doPost() {
        if (!App.getInstance().getManager(NetworkManager.class).isNetConect()) {
            try {
                this.onResult(ResultCode.RESULT_CODE_NET_ERROR, null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return;
        }
        String url = getUrl();
        Map<String, String> params = getParams();
        if (BuildConfig.DEBUG) {
            String string = "post request, url = " + url + "?params=" + params;
            string = string.replace("%", "%%");
            logger.i(string);
        }
        AsyncHttpClient.post(url, params, handler);
    }

    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        onPutParams(params);
        return params;
    }

    /**
     * 放入请求参数,data_list中的参数
     *
     * @param params 参数  基本参数已经放入
     */
    protected abstract void onPutParams(@NonNull Map<String, String> params);

    /**
     * 请求成功了  服务器已经返回结果
     *
     * @param code    请求成功的HTTP返回吗，，一般code等于200表示请求成功
     * @param content 从服务器获取到的数据
     */
    public abstract void onResult(int code, JSONObject content) throws JSONException;

    /**
     * 请求失败了
     *
     * @param exception 失败原因
     */
    public abstract void onError(Exception exception);

    @NonNull
    public abstract String getChildrenUrl();

    private String getUrl() {
        return Config.serverUrl + getChildrenUrl();
    }
}
