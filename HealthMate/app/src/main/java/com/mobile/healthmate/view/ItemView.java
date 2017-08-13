package com.mobile.healthmate.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobile.healthmate.R;
import com.mobile.healthmate.app.lib.viewinject.FindViewById;
import com.mobile.healthmate.app.lib.viewinject.ViewInjecter;

/**
 * Created by fujl on 2017/2/6.
 * icon标题内容控件
 */

public class ItemView extends LinearLayout {

    @FindViewById(R.id.icon)
    private ImageView icon;
    @FindViewById(R.id.title)
    private TextView title;
    @FindViewById(R.id.content)
    private TextView content;
    @FindViewById(R.id.arrow)
    private ImageView arrow;
    @FindViewById(R.id.cutline)
    private View cutline;

    public ItemView(Context context) {
        this(context, null);
    }

    public ItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemView);

        setIcon(typedArray.getDrawable(R.styleable.ItemView_icon));
        setTitle(typedArray.getString(R.styleable.ItemView_title));
        setContent(typedArray.getString(R.styleable.ItemView_content));
        setContentColor(typedArray.getColor(R.styleable.ItemView_contentcolor, getResources().getColor(R.color.textNormal)));
        setArrowVisiable(typedArray.getBoolean(R.styleable.ItemView_arrowvisiable, false));
        setCutlineVisiable(typedArray.getBoolean(R.styleable.ItemView_cutlinevisiable, false));

        typedArray.recycle();

    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_item, this, false);
        ViewInjecter.inject(this, view);
        this.addView(view);
    }

    public void setIcon(Drawable drawable) {
        icon.setImageDrawable(drawable);
    }

    public void setIcon(int id) {
        setIcon(getContext().getResources().getDrawable(id));
    }

    public void setTitle(String string) {
        title.setText(string);
    }

    public void setTitle(int id) {
        title.setText(id);
    }

    public void setContent(String string) {
        content.setText(string);
    }

    public String getContent() {
        return content.getText().toString();
    }

    public void setContent(int id) {
        content.setText(id);
    }

    public void setContentColor(int id) {
        content.setTextColor(id);
    }

    public void setArrowVisiable(boolean visiable) {
        if (visiable) {
            arrow.setVisibility(VISIBLE);
        } else {
            arrow.setVisibility(INVISIBLE);
        }
    }

    public void setCutlineVisiable(boolean visiable) {
        if (visiable) {
            cutline.setVisibility(VISIBLE);
        } else {
            cutline.setVisibility(INVISIBLE);
        }
    }

}
