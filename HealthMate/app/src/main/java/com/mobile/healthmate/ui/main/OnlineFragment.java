package com.mobile.healthmate.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobile.healthmate.R;
import com.mobile.healthmate.app.BaseFragment;
import com.mobile.healthmate.app.lib.viewinject.ViewInjecter;

/**
 * 在线看病
 * Created by fujl on 2017/8/15.
 */
public class OnlineFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_online, container, false);
        ViewInjecter.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
