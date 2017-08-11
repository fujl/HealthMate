package com.mobile.healthmate;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.mobile.healthmate.app.App;
import com.mobile.healthmate.app.BaseActivity;
import com.mobile.healthmate.app.Logger;
import com.mobile.healthmate.manager.user.UserManager;
import com.mobile.healthmate.ui.login.LoginActivity;

public class EntryActivity extends BaseActivity {
    public static final int REQUEST_CODE_PERMISSIONS_STORAGE = 14;
    public static final Logger logger = new Logger("WEB");
    private static final int HANDLER_WHAT_START = 2;
    @App.Manager
    private UserManager userManager;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == HANDLER_WHAT_START) {
                if (userManager.isLogin()) {
                    // 自动登录
                } else {
                    // 进入登录页
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                finish();
                return true;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logger.i("start");
        setContentView(R.layout.activity_entry);
        logger.i("end");
        checkStoragePermission();
    }

    // 检查权限
    private void checkStoragePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // 跳转相片胶卷页
            handler.sendEmptyMessageDelayed(HANDLER_WHAT_START, 1000);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.permission_title);
                builder.setMessage(R.string.permission_msg_storage);
                builder.setPositiveButton(R.string.permission_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 请求用户授权
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSIONS_STORAGE);
                    }
                });
                builder.show();
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSIONS_STORAGE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == EntryActivity.REQUEST_CODE_PERMISSIONS_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                showToast(R.string.permission_msg_storage_failed);
            }
            handler.sendEmptyMessageDelayed(HANDLER_WHAT_START, 1000);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
