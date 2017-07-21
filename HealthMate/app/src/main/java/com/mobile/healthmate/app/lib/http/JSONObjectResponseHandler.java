package com.mobile.healthmate.app.lib.http;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

public abstract class JSONObjectResponseHandler implements IResponseHandler {
    @Deprecated
    @Override
    public final void onResult(int code, byte[] data) {
        try {
            if(code == HttpURLConnection.HTTP_OK){
                String string = new String(data, "UTF-8");
                onResult(new JSONObject(string));
            } else{
                onError(new Exception("HTTP CODE IS NOT 200"));
            }
        } catch (JSONException | UnsupportedEncodingException e) {
            onError(e);
        }
    }

    public abstract void onResult(JSONObject jsonObject) throws JSONException;
}
