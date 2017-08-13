package com.mobile.healthmate.ui.main.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import com.mobile.healthmate.model.online.CmsContentModel;
import com.mobile.healthmate.view.AutoBannerView;
import com.mobile.healthmate.app.lib.imageloader.view.AsyncImageView;

/**
 * 首页banner适配器
 */

public class HomeBannerAdapter implements AutoBannerView.AutoBannerAdapter {

    private Context context;
    private List<CmsContentModel> list = new ArrayList<>();
    private OnBannerClickListener onBannerClickListener;

    public HomeBannerAdapter(Context context, List<CmsContentModel> list) {
        this.context = context;
        changeItems(list);
    }

    public void changeItems(List<CmsContentModel> list) {
        this.list.clear();
        this.list.addAll(list);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(View convertView, final int position) {
        AsyncImageView imageView;
        if (convertView != null) {
            imageView = (AsyncImageView) convertView;
        } else {
            imageView = new AsyncImageView(context);
            imageView.setBackgroundColor(Color.parseColor("#f7f7f7"));
        }
        imageView.loadUrlImage(list.get(position).getImgPath());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onBannerClickListener != null) {
                    onBannerClickListener.onClick(list.get(position));
                }
            }
        });
        return imageView;
    }

    /**
     * 设置banner点击事件
     */
    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {
        this.onBannerClickListener = onBannerClickListener;
    }


    public interface OnBannerClickListener {
        void onClick(CmsContentModel info);
    }
}
