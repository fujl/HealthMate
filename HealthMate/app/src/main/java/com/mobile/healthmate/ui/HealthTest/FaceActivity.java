package com.mobile.healthmate.ui.HealthTest;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.mobile.healthmate.BuildConfig;
import com.mobile.healthmate.R;
import com.mobile.healthmate.app.BaseActivity;
import com.mobile.healthmate.app.HConstant;
import com.mobile.healthmate.app.Logger;
import com.mobile.healthmate.app.lib.imageloader.BitmapUtils;
import com.mobile.healthmate.app.lib.viewinject.FindViewById;
import com.mobile.healthmate.app.lib.viewinject.ViewInjecter;
import com.mobile.healthmate.model.HealthTest.HealthReport;
import com.mobile.healthmate.util.AlbumUtils;
import com.mobile.healthmate.view.TopBar;

import java.util.concurrent.DelayQueue;

import static com.mobile.healthmate.app.HConstant.RESULT_CODE_REG_REPORT_FINISH;

public class FaceActivity extends BaseActivity {
    public static final String KEY_HEALTH_REPORT = "key_health_report";

    private HealthReport healthReport;

    /** 设置昵称数据传递key */
    private static final int REQUEST_CODE_PERMISSIONS_CAMERA = 1;

    private final String TAG = FaceActivity.class.getSimpleName();

    @FindViewById(R.id.test_right_btn)
    private TopBar mTopBar;

    // img_face
    @FindViewById(R.id.img_face)
    private ImageView imgFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);
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
        intent.setClass(this, TongueActivity.class);
        startActivityForResult(intent, 0);
    }

    public void onTakeFaceClick(View view) {
        checkCameraPermission();
    }

    /**
     * 检查相机权限
     */
    private void checkCameraPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            AlbumUtils.openCamera(FaceActivity.this, AlbumUtils.NORMAL);
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
            imgFace.setImageBitmap(bp);
        }

        if (resultCode == RESULT_CODE_REG_REPORT_FINISH) {
            setResult(RESULT_CODE_REG_REPORT_FINISH);
            finish();
        }
    }
}
