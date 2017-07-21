package com.mobile.healthmate.http.lib;

import com.mobile.healthmate.manager.ResultCode;

/**
 * HTTP请求结果 通用监听
 * 作为http请求回调使用的时候请使用OnHttpCodeListener代替
 * Created by zengdexing on 2017/1/12.
 */
@Deprecated
public interface OnHttpResponseListener<Data> extends ResultCode {
    /**
     * 请求完成监听
     *
     * @param code 请求返回结果
     * @param data 请求的数据
     */
    void onHttpResponse(int code, Data data);

}