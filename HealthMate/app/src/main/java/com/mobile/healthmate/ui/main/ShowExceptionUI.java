package com.mobile.healthmate.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.mobile.healthmate.BuildConfig;
import com.mobile.healthmate.R;
import com.mobile.healthmate.app.App;
import com.mobile.healthmate.app.BaseActivity;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


/**
 * 崩溃日志显示界面
 *
 * @author zdxing
 */
public class ShowExceptionUI extends BaseActivity {
    private TextView exceptionView;

    public static void showException(Throwable throwable) {
        App applicationContext = App.getInstance();
        if (applicationContext != null && BuildConfig.DEBUG) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            throwable.printStackTrace(new PrintStream(byteArrayOutputStream));
            String msg = new String(byteArrayOutputStream.toByteArray());

            Intent intent = new Intent(applicationContext, ShowExceptionUI.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("msg", msg);
            applicationContext.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_exception);
        exceptionView = (TextView) findViewById(R.id.show_exception_view);
        handlerIntent(getIntent(), false);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handlerIntent(intent, true);
    }

    private void handlerIntent(Intent intent, boolean isNew) {
        String msg = intent.getStringExtra("msg");
        if (msg != null) {
            if (isNew)
                exceptionView.append("\n\n\n\n\n\n");

            exceptionView.append(msg);
        }
    }
}
