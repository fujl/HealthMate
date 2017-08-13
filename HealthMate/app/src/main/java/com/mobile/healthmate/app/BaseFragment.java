package com.mobile.healthmate.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobile.healthmate.R;
import com.mobile.healthmate.manager.ResultCode;

/**
 * 所有的Fragment都必须继承自本类
 * Created by fujl on 2017/1/5.
 */
public class BaseFragment extends Fragment implements ResultCode {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().injectManager(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_base, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.textView);
        textView.setText(getClass().getSimpleName());

        return rootView;
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    public void showToast(String toast) {
        getBaseActivity().showToast(toast);
    }

    public void showToast(@StringRes int toast) {
        getBaseActivity().showToast(toast);
    }

    public void showLoadingProgressDialog() {
        getBaseActivity().showLoadingProgressDialog();
    }

    public void dismissLoadingProgressDialog() {
        getBaseActivity().dismissLoadingProgressDialog();
    }
}
