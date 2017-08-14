package com.mobile.healthmate.ui.HealthTest;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.mobile.healthmate.R;
import com.mobile.healthmate.app.BaseActivity;
import com.mobile.healthmate.app.HConstant;
import com.mobile.healthmate.app.lib.imageloader.BitmapUtils;
import com.mobile.healthmate.app.lib.viewinject.FindViewById;
import com.mobile.healthmate.app.lib.viewinject.ViewInjecter;
import com.mobile.healthmate.manager.report.ReportManager;
import com.mobile.healthmate.model.HealthTest.HealthReport;
import com.mobile.healthmate.util.AlbumUtils;
import com.mobile.healthmate.view.TopBar;

import static com.mobile.healthmate.app.HConstant.RESULT_CODE_REG_REPORT_FINISH;
import static com.mobile.healthmate.ui.HealthTest.FaceActivity.KEY_HEALTH_REPORT;

public class HandActivity extends BaseActivity {

    /** 设置昵称数据传递key */
    private static final int REQUEST_CODE_PERMISSIONS_CAMERA = 1;
    @FindViewById(R.id.test_right_btn)
    private TopBar mTopBar;
    @FindViewById(R.id.img_hand)
    private ImageView imgHand;
    private HealthReport healthReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand);

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
        ReportManager reportManager = getManager(ReportManager.class);
        boolean isSuccess = false;
        if (reportManager.generateReport(healthReport)) {
            isSuccess = true;
        } else {
            if (reportManager.randomGenerateReport()) {
                isSuccess = true;
            }
        }

        if (isSuccess) {
            setResult(RESULT_CODE_REG_REPORT_FINISH);
            finish();
        } else {
            Toast.makeText(this, "生成报告失败，请重试", Toast.LENGTH_SHORT).show();
        }
    }

    public void onTakeHandClick(View view) {
        checkCameraPermission();
    }

    /**
     * 检查相机权限
     */
    private void checkCameraPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            AlbumUtils.openCamera(HandActivity.this, AlbumUtils.NORMAL);
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
            imgHand.setImageBitmap(bp);
        }
    }
}
