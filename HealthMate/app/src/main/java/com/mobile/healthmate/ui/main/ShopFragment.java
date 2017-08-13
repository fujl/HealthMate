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
import com.mobile.healthmate.app.lib.viewinject.ViewInjecter;

/**
 * 闪电购物
 * Created by fujl on 2017/8/15.
 */
public class ShopFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_shop, container, false);
        ViewInjecter.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
