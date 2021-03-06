package com.mobile.healthmate.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * viewPager封装
 * Created by WangYu on 2016/10/22.
 */
@RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
public class NestScrollViewPager extends ViewPager {
    private boolean isScrollAble = true;//是否可滑动

    public NestScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestScrollViewPager(Context context) {
        this(context, null);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isScrollAble && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isScrollAble && super.onTouchEvent(ev);
    }

    public boolean isScrollAble() {
        return isScrollAble;
    }

    public void setScrollAble(boolean scrollAble) {
        isScrollAble = scrollAble;
    }
}
