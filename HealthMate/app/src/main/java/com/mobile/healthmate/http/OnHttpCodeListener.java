package com.mobile.healthmate.http;

import android.widget.Toast;

import com.mobile.healthmate.R;
import com.mobile.healthmate.app.App;
import com.mobile.healthmate.http.lib.OnHttpResponseListener;

/**
 *
 * Created by fujl on 2017/6/1.
 */


public class OnHttpCodeListener<Data> implements OnHttpResponseListener<Data> {

    @Override
    public void onHttpResponse(int code, Data data) {
        if (code == RESULT_CODE_TIME_OUT) {
            Toast.makeText(App.getInstance(), R.string.toast_time_out, Toast.LENGTH_SHORT).show();
        } else if (code == RESULT_CODE_NET_ERROR) {
            Toast.makeText(App.getInstance(), R.string.toast_net_error, Toast.LENGTH_SHORT).show();
        } else if (code != RESULT_CODE_OK && code != -1002 && code != -102) {
            onServerCode(code, data);
        }

    }

    public void onServerCode(int code, Data data) {
        Toast.makeText(App.getInstance(), R.string.toast_service_error, Toast.LENGTH_SHORT).show();
    }
}