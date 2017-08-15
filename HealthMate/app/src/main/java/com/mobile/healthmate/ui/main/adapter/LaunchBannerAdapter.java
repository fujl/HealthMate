package com.mobile.healthmate.ui.main.adapter;

import android.content.Context;
import android.graphics.Color;

import android.view.View;
import android.widget.ImageView;

import com.mobile.healthmate.view.AutoBannerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/15.
 *
 */

public class LaunchBannerAdapter implements AutoBannerView.AutoBannerAdapter {
    private Context context;
    private List<String> list = new ArrayList<>();
    private LaunchBannerAdapter.OnBannerClickListener onBannerClickListener;

    public LaunchBannerAdapter(Context context, List<String> list) {
        this.context = context;
        changeItems(list);
    }

    public void changeItems(List<String> list) {
        this.list.clear();
        this.list.addAll(list);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(View convertView, final int position) {
        ImageView imageView;
        if (convertView != null) {
            imageView = (ImageView) convertView;
        } else {
            imageView = new ImageView(context);
            imageView.setBackgroundColor(Color.parseColor("#f7f7f7"));
        }
        int imageId = Integer.parseInt(list.get(position));
        imageView.setImageResource(imageId);
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
    public void setOnBannerClickListener(LaunchBannerAdapter.OnBannerClickListener onBannerClickListener) {
        this.onBannerClickListener = onBannerClickListener;
    }


    public interface OnBannerClickListener {
        void onClick(String info);
    }
}
