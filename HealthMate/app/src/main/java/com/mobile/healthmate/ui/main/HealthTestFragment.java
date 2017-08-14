package com.mobile.healthmate.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobile.healthmate.R;
import com.mobile.healthmate.app.App;
import com.mobile.healthmate.app.BaseFragment;
import com.mobile.healthmate.app.lib.viewinject.FindViewById;
import com.mobile.healthmate.app.lib.viewinject.ViewInjecter;
import com.mobile.healthmate.manager.report.ReportManager;
import com.mobile.healthmate.model.HealthTest.HealthReport;
import com.mobile.healthmate.ui.HealthTest.HealthTestSubmitActivity;
import com.mobile.healthmate.ui.HealthTest.ReportActivity;
import com.mobile.healthmate.ui.main.adapter.HealthTestAdapter;
import com.mobile.healthmate.view.TopBar;
import com.mobile.healthmate.view.listview.OnPullRefreshListener;
import com.mobile.healthmate.view.listview.PullRefreshView;
import com.mobile.healthmate.view.listview.SimpleBaseAdapter;

import static com.mobile.healthmate.app.HConstant.RESULT_CODE_REG_REPORT_FINISH;
import static com.mobile.healthmate.ui.HealthTest.FaceActivity.KEY_HEALTH_REPORT;

/**
 * 健康测试
 * Created by fujl on 2017/8/15.
 */
public class HealthTestFragment extends BaseFragment {
    @FindViewById(R.id.test_right_btn)
    private TopBar mTopBar;

    @App.Manager
    private ReportManager reportManager;

    private PullRefreshView pullRefreshView;

    private HealthTestAdapter mAdapter;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_CODE_REG_REPORT_FINISH) {
            // 刷新界面
            mAdapter.setData(reportManager.reportList);
            pullRefreshView.startPullRefresh();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_health_test, container, false);
        initPullRefreshView(inflater, rootView);
        ViewInjecter.inject(this, rootView);
        registerListener();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void registerListener() {
        mTopBar.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), HealthTestSubmitActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    private void initPullRefreshView(LayoutInflater inflater, View rootView) {
        pullRefreshView = (PullRefreshView) rootView.findViewById(R.id.health_test_listview);

        pullRefreshView.setOnPullRefreshListener(new OnPullRefreshListener() {
            @Override
            public void onPullDownRefresh(final PullRefreshView pullRefreshView) {
                loadData();
            }
        });

        mAdapter = new HealthTestAdapter(this.getContext());
        mAdapter.setOnAdapterItemClickListener(new SimpleBaseAdapter.OnAdapterItemClickListener<HealthReport>() {
            @Override
            public void onAdapterItemClick(int position, HealthReport healthReport) {
                Intent intent = new Intent();
                intent.putExtra(KEY_HEALTH_REPORT, healthReport);
                intent.setClass(getContext(), ReportActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        pullRefreshView.setAdapter(mAdapter);
        pullRefreshView.startPullRefresh();
        loadData();
    }

    private void loadData() {
        if (pullRefreshView.isRefreshing()) {
            pullRefreshView.stopPullRefresh();
        }
        mAdapter.setData(reportManager.reportList);
    }
}
