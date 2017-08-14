package com.mobile.healthmate.ui.HealthTest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.mobile.healthmate.R;
import com.mobile.healthmate.app.lib.viewinject.FindViewById;
import com.mobile.healthmate.model.HealthTest.HealthReport;
import com.mobile.healthmate.ui.HealthTest.adapter.ReportItemAdapter;
import com.mobile.healthmate.ui.main.adapter.CmsContentAdapter;
import com.mobile.healthmate.view.listview.OnPullRefreshListener;
import com.mobile.healthmate.view.listview.PullRefreshView;

import static com.mobile.healthmate.ui.HealthTest.FaceActivity.KEY_HEALTH_REPORT;

public class ReportDetailActivity extends AppCompatActivity {

    private PullRefreshView pullRefreshView;

    private ReportItemAdapter mAdapter;

    private HealthReport healthReport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);
        healthReport = getIntent().getParcelableExtra(KEY_HEALTH_REPORT);
        initPullRefreshView();
    }

    private void initPullRefreshView() {
        pullRefreshView = (PullRefreshView)findViewById(R.id.report_item_listview);
        View headView = LayoutInflater.from(this).inflate(R.layout.view_report_detail_head, null);
        View footerView = LayoutInflater.from(this).inflate(R.layout.view_report_detail_tail, null);
        pullRefreshView.addHeaderView(headView);
        pullRefreshView.addFooterView(footerView);

        pullRefreshView.setOnPullRefreshListener(new OnPullRefreshListener() {
            @Override
            public void onPullDownRefresh(final PullRefreshView pullRefreshView) {
                loadData();
            }
        });

        mAdapter = new ReportItemAdapter(this);
        pullRefreshView.setAdapter(mAdapter);
        pullRefreshView.startPullRefresh();
        loadData();

    }

    private void loadData() {
        if (pullRefreshView.isRefreshing()) {
            pullRefreshView.stopPullRefresh();
        }
        mAdapter.setData(healthReport.getHealthReportItemList());
    }
}
