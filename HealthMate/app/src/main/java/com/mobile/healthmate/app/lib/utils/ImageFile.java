package com.mobile.healthmate.app.lib.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

/**
 * 读取相册图片 相册图片描述类
 *
 * @author zdxing 2015年1月27日
 */
public class ImageFile implements Parcelable {
    public static final Creator<ImageFile> CREATOR = new Creator<ImageFile>() {
        public ImageFile createFromParcel(Parcel source) {
            return new ImageFile(source);
        }

        public ImageFile[] newArray(int size) {
            return new ImageFile[size];
        }
    };
    private String displayName;
    private String bucketDisplayName;
    private String filePath;
    private long date;

    public ImageFile() {
    }

    protected ImageFile(Parcel in) {
        this.displayName = in.readString();
        this.bucketDisplayName = in.readString();
        this.filePath = in.readString();
        this.date = in.readLong();
    }

    /** 获取相册的所有图片 */
    public static ArrayList<ImageFile> getAllImage(Context context) {
        ArrayList<ImageFile> imgFiles = new ArrayList<>();
        String[] projection = new String[]{MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED};
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(uri, projection, "", null, "");
        try {
            if (cursor != null && cursor.moveToFirst()) {
                int folderColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                int fileNameColumn = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
                int pathColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                int dateColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
                do {
                    String folder = cursor.getString(folderColumn);
                    String fileName = cursor.getString(fileNameColumn);
                    String path = cursor.getString(pathColumn);
                    long date = cursor.getLong(dateColumn);

                    ImageFile imageFile = new ImageFile();
                    imageFile.setDate(date);
                    imageFile.setDisplayName(fileName);
                    imageFile.setFilePath(path);
                    imageFile.setBucketDisplayName(folder);

                    imgFiles.add(imageFile);
                } while (cursor.moveToNext());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return imgFiles;
    }

    public static ImageFile create(Parcel p) {
        ImageFile imageFile = new ImageFile();
        imageFile.setDisplayName(p.readString());
        imageFile.setBucketDisplayName(p.readString());
        imageFile.setFilePath(p.readString());
        imageFile.setDate(p.readLong());
        return imageFile;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getBucketDisplayName() {
        return bucketDisplayName;
    }

    public void setBucketDisplayName(String bucketDisplayName) {
        this.bucketDisplayName = bucketDisplayName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ImageFile [displayName=" + displayName + ", bucketDisplayName=" + bucketDisplayName + ", filePath="
                + filePath + ", date=" + date + "]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.displayName);
        dest.writeString(this.bucketDisplayName);
        dest.writeString(this.filePath);
        dest.writeLong(this.date);
    }
}
