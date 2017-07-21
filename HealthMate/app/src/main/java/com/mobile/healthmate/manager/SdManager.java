package com.mobile.healthmate.manager;

import android.os.Environment;
import android.text.TextUtils;

import com.mobile.healthmate.app.App;
import com.mobile.healthmate.app.BaseManager;

import java.io.File;

public class SdManager extends BaseManager {
    private final String TAG = "SdManager";

    /**
     * 主目录
     */
    private final String DIR_HEALTH_DOCTOR = "/healthDoctor/";

    /**
     * 临时目录
     */
    private final String DIR_TEMP = "temp/";
    /**
     * 消息图片
     */
    private final String DIR_SMS_IMAGE = "sms/img/";
    /**
     * 消息长文本
     */
    private final String DIR_SMS_TXTE = "sms/txt/";
    private final String DIR_AVATAR_IMAE = "avatar/img/";

    private String mHealthRootPath;
    private String mTempPath;
    private String mSmsImagePath;
    private String mSmsTextPath;
    private String mAvatarImagePath;


    /**
     * 获得系统sdcard路径
     */
    public static String getDirectoryPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 检测sdcard是否可用
     */
    public static boolean available() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static String getFileName(String url) {
        if (!TextUtils.isEmpty(url)) {
            return (url.hashCode() + "").replace("-", "_");
        } else {
            return "";
        }
    }

    public String getImagePath(String url) {
        return mHealthRootPath + "image/" + getFileName(url);
    }

    @Override
    public void onManagerCreate(App application) {
        mHealthRootPath = getDirectoryPath() + DIR_HEALTH_DOCTOR;
        mTempPath = mHealthRootPath + DIR_TEMP;
        mSmsImagePath = mHealthRootPath + DIR_SMS_IMAGE;
        mSmsTextPath = mHealthRootPath + DIR_SMS_TXTE;
        mAvatarImagePath = mHealthRootPath + DIR_AVATAR_IMAE;
        mkdir(mTempPath, mSmsImagePath, mSmsTextPath, mAvatarImagePath);
        init();
    }


    // 获取消息聊天，图片文件路径
    public String getSmsImgPath() {
        return mSmsImagePath;
    }

    /**
     * 消息长文本路径
     *
     * @return
     */
    public String getSmsTextPath() {
        return mSmsTextPath;
    }

    public File getAvatarImgFilePath(int uid) {
        return new File(mAvatarImagePath + uid + ".jpg");
    }


    private void init() {
        hintMediaFile(mTempPath, mSmsImagePath, mAvatarImagePath);
    }

    private void hintMediaFile(String... filePaths) {
        for (String filePath : filePaths) {
            try {
                if (filePath == null) {
                    break;
                }
                if (!filePath.endsWith(File.separator)) {
                    filePath = filePath + File.separatorChar;
                }
                File file = new File(filePath + ".nomedia");

                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public final synchronized boolean mkdir(String... filePaths) {
        try {
            for (String filePath : filePaths) {
                if (filePath == null) {
                    return false;
                }
                if (!filePath.endsWith(File.separator))
                    filePath = filePath.substring(0, filePath.lastIndexOf(File.separatorChar));
                File file = new File(filePath);
                if (!file.exists()) {
                    file.mkdirs();
                } else if (file.isFile()) {
                    file.delete();
                    file.mkdirs();
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized boolean mkdirs(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isDirectory()) {
            return true;
        } else {
            if (file.exists()) {
                file.delete();
            }
            file.mkdirs();
            return true;
        }
    }

    private void cleanFilePath(String filePath) {
        File f = new File(filePath);
        if (f.exists() && f.isDirectory() && f.listFiles().length > 0) {
            File delFile[] = f.listFiles();
            int i = delFile.length;
            for (int j = 0; j < i; j++) {
                if (delFile[j].isDirectory()) {
                    cleanFilePath(delFile[j].getAbsolutePath());
                }
                delFile[j].delete();
            }
        }
    }

    /**
     * 获取需要加载图片的绝对路径
     */
    public String getImgAbsolutePath(String fileName, String filePath) {
        return filePath + fileName;
    }


    public String getTempPath() {
        return mTempPath;
    }

    /**
     * 删除贵健康主目录文件
     */
    public void clear() {
        File directory = new File(mHealthRootPath);
        if (directory.exists()) {
            deleteFile(directory);
        }
    }

    private void deleteFile(File file) {
        if (file.isFile()) {
            deleteFileSafely(file);
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                deleteFileSafely(file);
                return;
            }
            for (File f : childFile) {
                deleteFile(f);
            }
            deleteFileSafely(file);
        }
    }

    /**
     * 安全删除文件（解决：open failed: EBUSY (Device or resource busy)）
     *
     * @param file
     * @return
     */
    private boolean deleteFileSafely(File file) {
        if (file != null) {
            String tmpPath = file.getParent() + File.separator + System.currentTimeMillis();
            File tmp = new File(tmpPath);
            file.renameTo(tmp);
            return tmp.delete();
        }
        return false;
    }

}
