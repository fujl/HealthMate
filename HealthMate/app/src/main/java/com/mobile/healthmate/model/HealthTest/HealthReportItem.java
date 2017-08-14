package com.mobile.healthmate.model.HealthTest;

import android.os.Parcel;
import android.os.Parcelable;

import com.mobile.healthmate.app.lib.json.JsonField;

/**
 * Created by fujl-mac on 2017/8/14.
 * 报告节点
 */

public class HealthReportItem implements Parcelable {
    public static final Parcelable.Creator<HealthReportItem> CREATOR = new Parcelable.Creator<HealthReportItem>() {
        @Override
        public HealthReportItem createFromParcel(Parcel source) {
            return new HealthReportItem(source);
        }

        @Override
        public HealthReportItem[] newArray(int size) {
            return new HealthReportItem[size];
        }
    };
    @JsonField("organ")
    private String organ;
    @JsonField("index")
    private String index;
    @JsonField("evaluate")
    private String evaluate;
    @JsonField("trend")
    private String trend;

    public HealthReportItem() {
    }

    protected HealthReportItem(Parcel in) {
        this.organ = in.readString();
        this.index = in.readString();
        this.evaluate = in.readString();
        this.trend = in.readString();
    }

    public String getOrgan() {
        return organ;
    }

    public void setOrgan(String organ) {
        this.organ = organ;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    public String getTrend() {
        return trend;
    }

    public void setTrend(String trend) {
        this.trend = trend;
    }

    @Override
    public String toString() {
        return "CmsContentModel{" +
                "gender=" + organ +
                ", age ='" + index + '\'' +
                ", cold =" + evaluate +
                ", sleep='" + trend + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.organ);
        dest.writeString(this.index);
        dest.writeString(this.evaluate);
        dest.writeString(this.trend);
    }
}
