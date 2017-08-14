package com.mobile.healthmate.ui.HealthTest;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;

import com.mobile.healthmate.R;
import com.mobile.healthmate.app.BaseActivity;
import com.mobile.healthmate.app.HConstant;
import com.mobile.healthmate.app.lib.imageloader.BitmapUtils;
import com.mobile.healthmate.app.lib.viewinject.FindViewById;
import com.mobile.healthmate.app.lib.viewinject.ViewInjecter;
import com.mobile.healthmate.model.HealthTest.HealthReport;
import com.mobile.healthmate.util.AlbumUtils;
import com.mobile.healthmate.view.TopBar;

import static com.mobile.healthmate.app.HConstant.RESULT_CODE_REG_REPORT_FINISH;
import static com.mobile.healthmate.ui.HealthTest.FaceActivity.KEY_HEALTH_REPORT;

public class TongueActivity extends BaseActivity {

    /** 设置昵称数据传递key */
    private static final int REQUEST_CODE_PERMISSIONS_CAMERA = 1;
    private final String TAG = FaceActivity.class.getSimpleName();
    private HealthReport healthReport;
    @FindViewById(R.id.test_right_btn)
    private TopBar mTopBar;

    @FindViewById(R.id.img_tongue)
    private ImageView imgTongue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tongue);

        ViewInjecter.inject(this);
        registerListener();

        healthReport = getIntent().getParcelableExtra(KEY_HEALTH_REPORT);
    }

    private void registerListener() {
        mTopBar.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickNext();
            }
        });
    }

    private void clickNext() {
        showLoadingProgressDialog();
        SystemClock.sleep(2000);
        dismissLoadingProgressDialog();

        Intent intent = new Intent();
        intent.putExtra(KEY_HEALTH_REPORT, healthReport);
        intent.setClass(this, HandActivity.class);
        startActivityForResult(intent, 0);
    }

    public void onTakeTongueClick(View view) {
        checkCameraPermission();
    }

    /**
     * 检查相机权限
     */
    private void checkCameraPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            AlbumUtils.openCamera(TongueActivity.this, AlbumUtils.NORMAL);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.permission_title);
                builder.setMessage(R.string.permission_msg_camera);
                builder.setPositiveButton(R.string.permission_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 请求用户授权
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSIONS_CAMERA);
                    }
                });
                builder.show();
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSIONS_CAMERA);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AlbumUtils.OPEN_CAMERA) {
            String imagePath = Environment.getExternalStorageDirectory() + "/" + HConstant.IMG_NAME_MASTER_AVATAR_TEMP;
            Bitmap bp = BitmapUtils.decodeFile(imagePath);
            imgTongue.setImageBitmap(bp);
        }

        if (resultCode == RESULT_CODE_REG_REPORT_FINISH) {
            setResult(RESULT_CODE_REG_REPORT_FINISH);
            finish();
        }
    }
}
