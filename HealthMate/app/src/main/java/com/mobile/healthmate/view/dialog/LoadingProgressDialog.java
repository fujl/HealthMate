package com.mobile.healthmate.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;

import com.mobile.healthmate.R;

/**
 * 公用页面加载对话框
 * Created by Lxx on 2017/1/9.
 */
public class LoadingProgressDialog extends Dialog {

    public LoadingProgressDialog(Context context) {
        super(context, R.style.LoadingProgressDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading_progress);
        getWindow().setGravity(Gravity.CENTER);
        setCancelable(false);
    }
}
