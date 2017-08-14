package com.mobile.healthmate.model.HealthTest;

import android.os.Parcel;
import android.os.Parcelable;

import com.mobile.healthmate.app.lib.json.JsonField;

import java.util.List;

/**
 * Created by fujl-mac on 2017/8/14.
 * 健康报告
 */

public class HealthReport implements Parcelable {
    public static final Parcelable.Creator<HealthReport> CREATOR = new Parcelable.Creator<HealthReport>() {
        @Override
        public HealthReport createFromParcel(Parcel source) {
            return new HealthReport(source);
        }

        @Override
        public HealthReport[] newArray(int size) {
            return new HealthReport[size];
        }
    };
    @JsonField("gender")
    private String gender;
    @JsonField("age_group")
    private String age;
    @JsonField("cold_case")
    private String cold;
    @JsonField("sweating_condition")
    private String sweating;
    @JsonField("pain_condition")
    private String pain;
    @JsonField("sleep_condition")
    private String sleep;
    @JsonField("sequence_number")
    private String sequenceNumber;
    @JsonField("health_index")
    private String healthIndex;
    @JsonField("list")
    private List<HealthReportItem> healthReportItemList;
    @JsonField("generate_dt")
    private String generateDt;

    public HealthReport() {
    }

    protected HealthReport(Parcel in) {
        this.gender = in.readString();
        this.age = in.readString();
        this.cold = in.readString();
        this.sweating = in.readString();
        this.pain = in.readString();
        this.sleep = in.readString();
        this.sequenceNumber = in.readString();
        this.healthIndex = in.readString();
        this.healthReportItemList = in.readArrayList(HealthReportItem.class.getClassLoader());
        this.generateDt = in.readString();
    }

    public String getGenerateDt() {
        return generateDt;
    }

    public void setGenerateDt(String generateDt) {
        this.generateDt = generateDt;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getHealthIndex() {
        return healthIndex;
    }

    public void setHealthIndex(String healthIndex) {
        this.healthIndex = healthIndex;
    }

    public List<HealthReportItem> getHealthReportItemList() {
        return healthReportItemList;
    }

    public void setHealthReportItemList(List<HealthReportItem> healthReportItemList) {
        this.healthReportItemList = healthReportItemList;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCold() {
        return cold;
    }

    public void setCold(String cold) {
        this.cold = cold;
    }

    public String getSweating() {
        return sweating;
    }

    public void setSweating(String sweating) {
        this.sweating = sweating;
    }

    public String getPain() {
        return pain;
    }

    public void setPain(String pain) {
        this.pain = pain;
    }

    public String getSleep() {
        return sleep;
    }

    public void setSleep(String sleep) {
        this.sleep = sleep;
    }

    @Override
    public String toString() {
        return "CmsContentModel{" +
                "gender=" + gender +
                ", age ='" + age + '\'' +
                ", cold =" + cold +
                ", sweating='" + sweating + '\'' +
                ", pain='" + pain + '\'' +
                ", sleep='" + sleep + '\'' +
                ", sequenceNumber='" + sequenceNumber + '\'' +
                ", healthIndex='" + healthIndex + '\'' +
                ", healthReportItemList='" + healthReportItemList.toString() + '\'' +
                ", generateDt='" + generateDt + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.gender);
        dest.writeString(this.age);
        dest.writeString(this.cold);
        dest.writeString(this.sweating);
        dest.writeString(this.pain);
        dest.writeString(this.sleep);
        dest.writeString(this.sequenceNumber);
        dest.writeString(this.healthIndex);
        dest.writeList(this.healthReportItemList);
        dest.writeString(this.generateDt);
    }
}
