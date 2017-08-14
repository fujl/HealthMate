package com.mobile.healthmate.ui.HealthTest.adapter;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.mobile.healthmate.R;
import com.mobile.healthmate.app.lib.viewinject.FindViewById;
import com.mobile.healthmate.model.HealthTest.HealthReport;
import com.mobile.healthmate.model.HealthTest.HealthReportItem;
import com.mobile.healthmate.ui.main.adapter.HealthTestAdapter;
import com.mobile.healthmate.view.listview.SimpleBaseAdapter;

/**
 * Created by fujl-mac on 2017/8/14.
 *
 */

public class ReportItemAdapter extends SimpleBaseAdapter<HealthReportItem, ReportItemAdapter.ViewHolder> {
    public ReportItemAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.adapter_health_report_item;
    }

    @Override
    protected void bindView(ReportItemAdapter.ViewHolder viewHolder, HealthReportItem report, int position) {
        viewHolder.organText.setText(report.getOrgan());
        viewHolder.indexText.setText(report.getIndex());
        viewHolder.evaluateText.setText(report.getEvaluate());
        viewHolder.trendText.setText(report.getTrend());
        if (position%2!=0) {
            viewHolder.bgView.setBackgroundColor(this.context.getResources().getColor(R.color.white));
        } else {
            viewHolder.bgView.setBackgroundColor(this.context.getResources().getColor(R.color.app_bg));
        }
    }

    @NonNull
    @Override
    protected ReportItemAdapter.ViewHolder onNewViewHolder() {
        return new ReportItemAdapter.ViewHolder();
    }

    static class ViewHolder {
        @FindViewById(R.id.item_bg)
        View bgView;
        @FindViewById(R.id.organ)
        TextView organText;
        @FindViewById(R.id.index)
        TextView indexText;
        @FindViewById(R.id.evaluate)
        TextView evaluateText;
        @FindViewById(R.id.trend)
        TextView trendText;
    }
}
