package com.mobile.healthmate.app.lib.http;

public interface IResponseHandler {
    public void onResult(int code, byte[] data);

    public void onError(Exception exception);
}
