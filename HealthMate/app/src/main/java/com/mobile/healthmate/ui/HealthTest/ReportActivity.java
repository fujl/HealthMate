package com.mobile.healthmate.ui.HealthTest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.mobile.healthmate.R;
import com.mobile.healthmate.app.BaseActivity;
import com.mobile.healthmate.app.lib.viewinject.FindViewById;
import com.mobile.healthmate.app.lib.viewinject.ViewInjecter;
import com.mobile.healthmate.model.HealthTest.HealthReport;

import java.util.ArrayList;

import static com.mobile.healthmate.ui.HealthTest.FaceActivity.KEY_HEALTH_REPORT;

public class ReportActivity extends BaseActivity {

    @FindViewById(R.id.spread_pie_chart)
    private PieChart mChart;

    private HealthReport healthReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ViewInjecter.inject(this);
        healthReport = getIntent().getParcelableExtra(KEY_HEALTH_REPORT);
        PieData mPieData = getPieData();
        showChart(mChart, mPieData);
    }

    private void showChart(PieChart pieChart, PieData pieData) {
        pieChart.setHoleRadius(60f); // 半径
        pieChart.setTransparentCircleRadius(64f); // 半透明圈
        pieChart.setDrawCenterText(true); // 饼状图中间可以添加文字
        pieChart.setDrawHoleEnabled(true);
        pieChart.setRotationAngle(90); // 初始旋转角度
        pieChart.setRotationEnabled(true); // 可以手动旋转

        // TODO: 饼状图中间的文字
        pieChart.setCenterText(healthReport.getHealthIndex());
        pieChart.setCenterTextSize(26f);

        // 设置数据
        pieChart.setData(pieData);

        Legend mLegend = pieChart.getLegend(); // 设置比例图
        mLegend.setEnabled(false); // 禁用比例块

        pieChart.animateXY(1000, 1000); // 设置动画
    }

    /**
     * 构建数据
     *
     * @return
     * @author zhangxingxing
     * @date 2017年8月13日 下午2:36:49
     */
    private PieData getPieData() {

        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>(); // yVals用来表示封装每个饼块的实际数据

        // TODO: 饼图数据
        float index = Float.parseFloat(healthReport.getHealthIndex());
        yValues.add(new PieEntry(100 - index, 0));
        yValues.add(new PieEntry(index, 1));

        // y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, ""/* 显示在比例图上 */);
        pieDataSet.setValueTextColor(Color.rgb(255, 255, 255));
        pieDataSet.setSliceSpace(0f); // 设置个饼状图之间的距离

        ArrayList<Integer> colors = new ArrayList<Integer>();

        // 饼图颜色
        colors.add(Color.rgb(9 * 16 + 8, 12 * 16 + 13, 15 * 16 + 9));  // 98cef9
        colors.add(Color.rgb(getR(), getG(), getB()));

        pieDataSet.setColors(colors);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度

        PieData pieData = new PieData(pieDataSet);

        return pieData;
    }

    private int getR() {

        int index = Integer.parseInt(healthReport.getHealthIndex());
        if (index >= 90) {
            return 6 * 16 + 4;
        } else if (index < 90 && index >= 70) {
            return 5 * 16;
        } else if (index < 70 && index >= 50) {
            return 13 * 16 + 12;
        } else {
            return 13 * 16 + 5;
        }
    }

    private int getG() {
        float index = Integer.parseInt(healthReport.getHealthIndex());
        if (index >= 90) {
            return 12 * 16 + 9;
        } else if (index < 90 && index >= 70) {
            return 8 * 16 + 7;
        } else if (index < 70 && index >= 50) {
            return 11 * 16 + 10;
        } else {
            return 5 * 16 + 4;
        }
    }

    private int getB() {
        float index = Integer.parseInt(healthReport.getHealthIndex());
        if (index >= 90) {
            return 9 * 16;
        } else if (index < 90 && index >= 70) {
            return 13 * 16 + 9;
        } else if (index < 70 && index >= 50) {
            return 3 * 16 + 13;
        } else {
            return 5 * 16 + 2;
        }
    }

    public void onReportDetailClick(View view) {
        Intent intent = new Intent();
        intent.putExtra(KEY_HEALTH_REPORT, healthReport);
        intent.setClass(this, ReportDetailActivity.class);
        startActivityForResult(intent, 0);
    }

//    - (UIColor *)getHealthColor:(CGFloat)healthIndex {
//        if (healthIndex >= 90) {
//            return [UIColor colorWithRGB:0x64C990];
//        } else if (healthIndex < 90 && healthIndex >= 70) {
//            return [UIColor colorWithRGB:0x5087d9];
//        } else if (healthIndex < 70 && healthIndex >= 50) {
//            return [UIColor colorWithRGB:0xdcba3d];
//        } else {
//            return [UIColor colorWithRGB:0xd75452];
//        }
//    }
}
