package com.mobile.healthmate.http.lib;

import com.mobile.healthmate.http.OnHttpCodeListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 简单请求
 * <p>
 * 如果请求返回数据过多，OnHttpResponseListener不能适用，可直接使用HlwyyRequester
 * <p>
 * Created by fujl-mac on 2017/7/17.
 */

public abstract class SimpleRequester<Data> extends HttpRequester {
    protected OnHttpCodeListener<Data> onHttpCodeListener;

    public  SimpleRequester(OnHttpCodeListener<Data> onHttpCodeListener) {
        this.onHttpCodeListener = onHttpCodeListener;
    }

    @Override
    public void onResult(int code, JSONObject content) throws JSONException {
        Data data = null;
        if (code == OnHttpCodeListener.RESULT_CODE_OK) {
            data = onDumpData(content);
        }
        onHttpCodeListener.onHttpResponse(code, data);
    }

    @Override
    public void onError(Exception exception) {
        exception.printStackTrace();

        onHttpCodeListener.onHttpResponse(OnHttpCodeListener.RESULT_CODE_TIME_OUT, null);
    }

    public abstract Data onDumpData(JSONObject jsonObject) throws JSONException;
}
