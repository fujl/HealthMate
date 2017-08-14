//$_FILEHEADER_BEGIN ***************************
//版权声明: 贵阳朗玛信息技术股份有限公司版权所有
//Copyright (C) 2012 Longmaster Corporation. All Rights Reserved
//文件名称: AlbumUtils.java
//创建日期: 2013/03/20
//创 建 人: czc
//文件说明: 
//$_FILEHEADER_END *****************************
package com.mobile.healthmate.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import com.mobile.healthmate.R;
import com.mobile.healthmate.app.App;
import com.mobile.healthmate.app.HConstant;
//import cn.longmaster.health.app.HApplication;
//import cn.longmaster.health.app.HConstant;
//import cn.longmaster.health.manager.health39.healthData;
//import cn.longmaster.health.ui.old.PictureCutUI;

public class AlbumUtils {
    public static final int BLOGSETTING = 2;
    public static final int NORMAL = 1;
    public static final int OPEN_CAMERA = 1;
    public static final int OPEN_ALBUM = 2;
    public static final int PHOTO_RESULT = 3;
    public static final int OPEN_CAMERA_EX = 4;
    public static final int OPEN_ALBUM_EX = 5;
    private static final String TAG = "AlbumUtils";

    /**
     * 判断Intent转向的应用组件是否存在
     *
     * @param a_oContext
     * @param a_oIntent  隐式调用的Intent
     * @return
     */
    public static boolean isIntentExist(Context a_oContext, Intent a_oIntent) {
        boolean isExist = false;
        // PackageManager是用于判断Intent转向是否成功的管理器
        PackageManager packageManager = a_oContext.getPackageManager();
        List<ResolveInfo> componentList = packageManager.queryIntentActivities(a_oIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (componentList.size() > 0) {
            isExist = true;
        }

        return isExist;
    }

    /**
     * 打开相册
     */
    public static void openAlbum(Activity a_Activity, int a_type) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(App.getInstance(), R.string.insertsdcard, Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");

        if (isIntentExist(App.getInstance(), intent)) {
            if (a_type == NORMAL) {
                a_Activity.startActivityForResult(intent, OPEN_ALBUM);
            } else {
                a_Activity.startActivityForResult(intent, OPEN_ALBUM_EX);
            }
        } else {
            Toast.makeText(App.getInstance(), R.string.notsupport, Toast.LENGTH_LONG).show();
        }
    }

    public static boolean SupportFileType(String a_strUri) {
        if (a_strUri == null) {
            return false;
        }
        String lowStr = a_strUri.toLowerCase();
        return !(lowStr.endsWith("gif") || lowStr.endsWith("mp4") || lowStr.endsWith("rmvb") || lowStr.endsWith("rm") || lowStr.endsWith("asf")
                || lowStr.endsWith("wmv") || lowStr.endsWith("avi") || lowStr.endsWith("mpeg") || lowStr.endsWith("rmvb") || lowStr.endsWith("3gp")
                || lowStr.endsWith("mkv") || lowStr.endsWith("m4v") || lowStr.endsWith("mov") || lowStr.endsWith("flv") || lowStr.endsWith("asx"));
    }

    public static String getDirFromAlbumUri(Uri a_uri) {
        String path = null;
        Cursor l_Cursor = null;
        try {
            String[] projection = new String[]{MediaStore.Images.Media.DATA};
            l_Cursor = App.getInstance().getContentResolver().query(a_uri, projection, "", null, "");
            l_Cursor.moveToFirst();
            path = l_Cursor.getString(l_Cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        } catch (Exception e) {
            path = a_uri.toString().replace("file://", "");
        } finally {
            if (l_Cursor != null)
                l_Cursor.close();
        }
        return path;
    }

    /**
     * 打开相机
     */
    public static void openCamera(Activity a_oActivity, int a_type) {
        File file = new File(Environment.getExternalStorageDirectory(), HConstant.IMG_NAME_MASTER_AVATAR_TEMP);
        if (file.exists())
            file.delete();
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            uri = a_oActivity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        } else {
            uri = Uri.fromFile(file);
        }
        openCamera(a_oActivity, a_type, uri);
    }

    /**
     * 打开相机
     * Uri在Android N上的使用有修改,使用时需要注意.
     * 建议使用openCamera(Activity a_oActivity, int a_type)
     * 如需要自定义uri，请对Android N进行兼容
     */
    @Deprecated
    public static void openCamera(Activity a_oActivity, int a_type, Uri output) {
        if (!StorageUtils.checkSdcard()) {
            Toast.makeText(App.getInstance(), R.string.insertsdcard, Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
        // 判断该设备是否支持打开相机的操作
        if (isIntentExist(App.getInstance(), intent)) {
            if (a_type == NORMAL) {
                a_oActivity.startActivityForResult(intent, OPEN_CAMERA);
            } else {
                a_oActivity.startActivityForResult(intent, OPEN_CAMERA_EX);
            }
        } else {
            Toast.makeText(App.getInstance(), R.string.notsupport, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 剪切图片
     *
     * @param a_path
     * @param a_oActivity
     */
    public static void startPhotoZoom(String a_path, Activity a_oActivity) {
        if (a_path == null || "".equals(a_path))
            return;
//        Intent intent = new Intent(a_oActivity, PictureCutUI.class);
//        intent.putExtra(PictureCutUI.PATH_STRING, a_path);
//        a_oActivity.startActivityForResult(intent, PHOTO_RESULT);
    }

    /**
     * 返回相册图片的路径
     *
     * @return 图片路径或null
     */
    public static String getAlbumImagePath(Context context, Uri uri) {
        String lImagePath = null;
        try {
            String[] lMediaStoreData = {MediaStore.Images.Media.DATA, MediaStore.Images.ImageColumns.ORIENTATION};
            Cursor lCursor = context.getContentResolver().query(uri, lMediaStoreData, null, null, null);

            if (lCursor != null) {
                if (lCursor.moveToPosition(0)) {
                    int columnPathIndex = lCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    lImagePath = lCursor.getString(columnPathIndex);
                }
                lCursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return lImagePath;
    }
}
