package com.mobile.healthmate.ui.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.mobile.healthmate.R;
import com.mobile.healthmate.app.lib.viewinject.FindViewById;
import com.mobile.healthmate.model.HealthTest.HealthReport;
import com.mobile.healthmate.view.listview.SimpleBaseAdapter;

/**
 * Created by fujl-mac on 2017/8/14.
 */

public class HealthTestAdapter extends SimpleBaseAdapter<HealthReport, HealthTestAdapter.ViewHolder> {

    public HealthTestAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.adapter_health_test;
    }

    @Override
    protected void bindView(HealthTestAdapter.ViewHolder viewHolder, HealthReport report, int position) {
        String title = report.getGender() + report.getAge() + report.getCold() + report.getSweating() + report.getPain() + report.getSleep();
        viewHolder.titleText.setText(title);

        viewHolder.createTimeText.setText(report.getGenerateDt());
    }

    @NonNull
    @Override
    protected HealthTestAdapter.ViewHolder onNewViewHolder() {
        return new HealthTestAdapter.ViewHolder();
    }

    static class ViewHolder {
        @FindViewById(R.id.ht_title)
        TextView titleText;

        @FindViewById(R.id.ht_create_time)
        TextView createTimeText;
    }
}


