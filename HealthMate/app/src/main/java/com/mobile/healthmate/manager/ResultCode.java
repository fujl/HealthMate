package com.mobile.healthmate.manager;

/**
 * 服务器返回结果定义
 * Created by fujl-mac on 2017/7/11.
 */

public interface ResultCode {
    /** 操作成功 */
    int RESULT_CODE_OK = 2;

    /** 无网络 */
    int RESULT_CODE_NET_ERROR = -10000;

    /** 请求超时 */
    int RESULT_CODE_TIME_OUT = -10001;
}
