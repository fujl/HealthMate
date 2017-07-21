package com.mobile.healthmate.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;

/**
 * 支持两种状态图片切换
 * Created by fujl-mac on 2017/7/12.
 */

public class CheckableImageView extends ImageView implements Checkable {
    private boolean mChecked = false;
    private OnCheckedChangeListener onCheckedChangeListener;

    public CheckableImageView(Context context) {
        this(context, null);
    }

    public CheckableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setChecked(!mChecked);
                if (onCheckedChangeListener != null) {
                    onCheckedChangeListener.onCheckedChanged(CheckableImageView.this, mChecked);
                }
            }
        });
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            refreshDrawableState();
            if (onCheckedChangeListener != null) {
                onCheckedChangeListener.onCheckedChanged(CheckableImageView.this, mChecked);
            }
        }
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, new int[]{android.R.attr.state_checked});
        }
        return drawableState;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(View view, boolean isChecked);
    }
}
