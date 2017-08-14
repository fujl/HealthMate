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
import com.mobile.healthmate.http.OnHttpCodeListener;
import com.mobile.healthmate.http.ResultList;
import com.mobile.healthmate.http.online.CmsContentRequester;
import com.mobile.healthmate.model.online.CmsContentModel;
import com.mobile.healthmate.ui.HealthTest.HealthTestSubmitActivity;
import com.mobile.healthmate.ui.main.adapter.HomeBannerAdapter;
import com.mobile.healthmate.ui.main.adapter.CmsContentAdapter;
import com.mobile.healthmate.view.AutoBannerView;
import com.mobile.healthmate.view.listview.OnPullRefreshListener;
import com.mobile.healthmate.view.listview.PullRefreshView;

import java.util.ArrayList;
import java.util.List;

/**
 * 在线看病
 * Created by fujl on 2017/8/15.
 */
public class OnlineFragment extends BaseFragment {

    /**
     * 首页的ListView
     */
    private PullRefreshView pullRefreshView;

    private CmsContentAdapter mAdapter;

    @FindViewById(R.id.banner_gallery)
    private AutoBannerView mBannerGalleryView;

    private HomeBannerAdapter mBannerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_online, container, false);
        initPullRefreshView(inflater, rootView);
        ViewInjecter.inject(this, rootView);
        initSubview();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void loadData() {
        new CmsContentRequester(new OnHttpCodeListener<ResultList<CmsContentModel>>() {
            @Override
            public void onHttpResponse(int code, ResultList<CmsContentModel> cmsContentModelResultList) {
                if (code == RESULT_CODE_OK) {
                    if (pullRefreshView.isRefreshing()) {
                        pullRefreshView.stopPullRefresh();
                    }
                    ArrayList<CmsContentModel> filterList = new ArrayList<CmsContentModel>();
                    for (CmsContentModel mdl : cmsContentModelResultList.getList()) {
                        if (mdl.getCcId() != 14) {
                            filterList.add(mdl);
                        }
                    }
                    mAdapter.setData(filterList);
                    mBannerAdapter.changeItems(cmsContentModelResultList.getList());
                    mBannerGalleryView.setAdapter(mBannerAdapter);
                }
            }
        }).doPost();
    }

    private void initSubview(){
        mBannerGalleryView.setWaitMilliSceond(3500);
        mBannerAdapter = new HomeBannerAdapter(getContext(), new ArrayList<CmsContentModel>());
        mBannerAdapter.setOnBannerClickListener(new HomeBannerAdapter.OnBannerClickListener() {
            @Override
            public void onClick(CmsContentModel info) {
                // umeng点击事件
//                mobClickManager.onHomeClick("banner", String.valueOf(info.getId()));
//                CommonUtils.sendClickCount(ClickCountRequest.MODULE_TYPE_HOME_TOP_BANNER, String.valueOf(info.getId()));
//                HomeDataManager homeDataManager = HApplication.getInstance().getManager(HomeDataManager.class);
//                homeDataManager.startActivity(info.getJumpType(), info.getAppColumn(), info.getContent(), getContext());
                if (info.getCcId() == 14) {
                    Intent intent = new Intent();
                    intent.setClass(getContext(), HealthTestSubmitActivity.class);
                    startActivityForResult(intent, 0);
                }
            }
        });
    }

    private void initPullRefreshView(LayoutInflater inflater, View rootView) {
        pullRefreshView = (PullRefreshView)rootView.findViewById(R.id.home_listview);
        View view = inflater.inflate(R.layout.fragment_new_online, pullRefreshView, false);
        pullRefreshView.addHeaderView(view);

        pullRefreshView.setOnPullRefreshListener(new OnPullRefreshListener() {
            @Override
            public void onPullDownRefresh(final PullRefreshView pullRefreshView) {
                loadData();
            }
        });

        mAdapter = new CmsContentAdapter(this.getContext());
        pullRefreshView.setAdapter(mAdapter);
        pullRefreshView.startPullRefresh();
        loadData();

    }
}
