package com.mobile.healthmate.ui.main.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.mobile.healthmate.R;
import com.mobile.healthmate.app.App;
import com.mobile.healthmate.app.lib.imageloader.view.AsyncImageView;
import com.mobile.healthmate.app.lib.viewinject.FindViewById;
import com.mobile.healthmate.manager.SdManager;
import com.mobile.healthmate.model.online.CmsContentModel;
import com.mobile.healthmate.view.listview.SimpleBaseAdapter;

/**
 * Created by Administrator on 2017/8/13.
 */

public class CmsContentAdapter extends SimpleBaseAdapter<CmsContentModel, CmsContentAdapter.ViewHolder> {

    public CmsContentAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.adapter_cms_content;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, CmsContentModel cmsContentModel, int position) {
        viewHolder.titleText.setText(cmsContentModel.getTitle());
        viewHolder.contentText.setText(cmsContentModel.getShortTitle());

        viewHolder.createTimeText.setText(cmsContentModel.getCreateTime());

        String filePath = App.getInstance().getManager(SdManager.class).getImagePath(cmsContentModel.getImgPath());
        viewHolder.imageView.loadImage(filePath, cmsContentModel.getImgPath());
    }

    @NonNull
    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    static class ViewHolder {
        @FindViewById(R.id.image_view)
        AsyncImageView imageView;

        @FindViewById(R.id.cc_title)
        TextView titleText;

        @FindViewById(R.id.cc_content)
        TextView contentText;

        @FindViewById(R.id.cc_create_time)
        TextView createTimeText;
    }
}
