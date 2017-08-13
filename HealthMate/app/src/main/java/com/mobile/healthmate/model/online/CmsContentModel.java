package com.mobile.healthmate.model.online;

import android.os.Parcel;
import android.os.Parcelable;

import com.mobile.healthmate.app.lib.json.JsonField;

/**
 * Created by Administrator on 2017/8/13.
 * 内容列表
 */

public class CmsContentModel implements Parcelable {
    public int getCcId() {
        return ccId;
    }

    public void setCcId(int ccId) {
        this.ccId = ccId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    @JsonField("id")
    private int ccId;

    @JsonField("createTime")
    private String createTime;

    @JsonField("title")
    private String title;

    @JsonField("shortTitle")
    private String shortTitle;

    @JsonField("txt")
    private String txt;

    @JsonField("imgPath")
    private String imgPath;

    @Override
    public String toString() {
        return "CmsContentModel{" +
                "ccId=" + ccId +
                ", createTime ='" + createTime + '\'' +
                ", title =" + title +
                ", shortTitle='" + shortTitle + '\'' +
                ", txt='" + txt + '\'' +
                ", imgPath='" + imgPath + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ccId);
        dest.writeString(this.createTime);
        dest.writeString(this.title);
        dest.writeString(this.shortTitle);
        dest.writeString(this.txt);
        dest.writeString(this.imgPath);
    }

    public CmsContentModel() {
    }

    protected CmsContentModel(Parcel in) {
        this.ccId = in.readInt();
        this.createTime = in.readString();
        this.title = in.readString();
        this.shortTitle = in.readString();
        this.txt = in.readString();
        this.imgPath = in.readString();
    }

    public static final Parcelable.Creator<CmsContentModel> CREATOR = new Parcelable.Creator<CmsContentModel>() {
        @Override
        public CmsContentModel createFromParcel(Parcel source) {
            return new CmsContentModel(source);
        }

        @Override
        public CmsContentModel[] newArray(int size) {
            return new CmsContentModel[size];
        }
    };
}
