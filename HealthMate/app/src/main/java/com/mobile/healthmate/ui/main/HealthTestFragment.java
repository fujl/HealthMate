package com.mobile.healthmate.ui.main;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobile.healthmate.R;
import com.mobile.healthmate.app.BaseFragment;
import com.mobile.healthmate.app.lib.viewinject.FindViewById;
import com.mobile.healthmate.app.lib.viewinject.ViewInjecter;
import com.mobile.healthmate.ui.HealthTest.HealthTestSubmitActivity;
import com.mobile.healthmate.view.TopBar;

/**
 * 健康测试
 * Created by fujl on 2017/8/15.
 */
public class HealthTestFragment extends BaseFragment {
    @FindViewById(R.id.test_right_btn)
    private TopBar mTopBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_health_test, container, false);
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
                //HealthTestSubmitActivity.startActivity(getContext(), null);
            }
        });
    }
}
