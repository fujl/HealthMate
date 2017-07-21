package com.mobile.healthmate.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobile.healthmate.R;
import com.mobile.healthmate.app.lib.viewinject.FindViewById;
import com.mobile.healthmate.app.lib.viewinject.InjectOnClickListener;
import com.mobile.healthmate.app.lib.viewinject.ViewInjecter;

import java.lang.reflect.Method;

/**
 * 顶部工具栏
 * Created by zdxing on 15/12/27.
 */
public class TopBar extends RelativeLayout {

    public static final int FUNCTION_NONE = 0;
    public static final int FUNCTION_LEFT_TITLE = 2;
    public static final int FUNCTION_LEFT_BTN = 4;
    public static final int FUNCTION_TITLE = 8;
    public static final int FUNCTION_RIGHT_TITLE = 16;
    public static final int FUNCTION_RIGHT_BTN = 32;

    private int function = -1;

    @FindViewById(R.id.top_bar_left_title)
    private TextView leftTextView;

    @FindViewById(R.id.top_bar_title)
    private TextView titleTextView;

    @FindViewById(R.id.top_bar_right_title)
    private TextView rightTextView;

    @FindViewById(R.id.top_bar_left_btn)
    private ImageButton leftIconBtn;

    @FindViewById(R.id.top_bar_right_btn)
    private ImageButton rightIconBtn;

    public TopBar(Context context) {
        this(context, null);
    }

    public TopBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View topBarView = LayoutInflater.from(context).inflate(R.layout.view_top_bar, this, false);

        ViewInjecter.inject(this, topBarView);

        this.addView(topBarView);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TopBar);

        String title = typedArray.getString(R.styleable.TopBar_titleText);
        if (title == null)
            title = getResources().getString(R.string.app_name);
        setTitle(title);

        setLeftText(typedArray.getString(R.styleable.TopBar_leftText));
        setRightText(typedArray.getString(R.styleable.TopBar_rightText));

        Drawable leftDrawable = typedArray.getDrawable(R.styleable.TopBar_leftIcon);
        if (leftDrawable == null)
            leftDrawable = getResources().getDrawable(R.drawable.ic_back);
        setLeftDrawable(leftDrawable);

        setRightDrawable(typedArray.getDrawable(R.styleable.TopBar_rightIcon));

        int function = typedArray.getInt(R.styleable.TopBar_function, FUNCTION_LEFT_BTN | FUNCTION_TITLE);
        setFunction(function);

        String onLeftClick = typedArray.getString(R.styleable.TopBar_leftOnClick);
        if (onLeftClick == null) {
            onLeftClick = "onBackClick";
        }
        try {
            Method method = context.getClass().getMethod(onLeftClick, View.class);
            setOnLeftClickListener(new InjectOnClickListener(method, context));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        String onRightClick = typedArray.getString(R.styleable.TopBar_rightOnClick);
        if (onRightClick != null) {
            try {
                Method method = context.getClass().getMethod(onRightClick, View.class);
                setOnRightClickListener(new InjectOnClickListener(method, context));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        String onTitleClick = typedArray.getString(R.styleable.TopBar_titleOnClick);
        if (onTitleClick != null) {
            try {
                Method method = context.getClass().getMethod(onTitleClick, View.class);
                setOnTitleClickListener(new InjectOnClickListener(method, context));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        typedArray.recycle();
    }

    public void setOnLeftClickListener(OnClickListener onLeftClickListener) {
        leftIconBtn.setOnClickListener(onLeftClickListener);
        leftTextView.setOnClickListener(onLeftClickListener);

        leftIconBtn.setEnabled(onLeftClickListener != null);
        leftTextView.setEnabled(onLeftClickListener != null);
    }

    public void setOnRightClickListener(OnClickListener onRightClickListener) {
        rightIconBtn.setOnClickListener(onRightClickListener);
        rightTextView.setOnClickListener(onRightClickListener);

        rightIconBtn.setEnabled(onRightClickListener != null);
        rightTextView.setEnabled(onRightClickListener != null);
    }

    public void setOnTitleClickListener(OnClickListener onTitleClickListener) {
        titleTextView.setOnClickListener(onTitleClickListener);
        titleTextView.setEnabled(onTitleClickListener != null);
    }

    /**
     * 添加某一个功能
     */
    public void addFunction(int function) {
        setFunction(this.function | function);
    }

    public boolean isAddFunction(int function) {
        return (this.function & function) == function;
    }

    public void removeFunction(int function) {
        setFunction(this.function & (~function));
    }

    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    public void setLeftText(String title) {
        leftTextView.setText(title);
    }

    public void setRightText(String title) {
        rightTextView.setText(title);
    }

    public void setLeftDrawable(Drawable drawable) {
        leftIconBtn.setImageDrawable(drawable);
    }

    public void setRightDrawable(Drawable drawable) {
        rightIconBtn.setImageDrawable(drawable);
    }

    public void setFunction(int function) {
        if (this.function == function) {
            return;
        }
        this.function = function;

        leftTextView.setVisibility(isAddFunction(FUNCTION_LEFT_TITLE) ? VISIBLE : INVISIBLE);

        leftIconBtn.setVisibility(isAddFunction(FUNCTION_LEFT_BTN) ? VISIBLE : INVISIBLE);

        titleTextView.setVisibility(isAddFunction(FUNCTION_TITLE) ? VISIBLE : INVISIBLE);

        rightTextView.setVisibility(isAddFunction(FUNCTION_RIGHT_TITLE) ? VISIBLE : INVISIBLE);

        rightIconBtn.setVisibility(isAddFunction(FUNCTION_RIGHT_BTN) ? VISIBLE : INVISIBLE);
    }

    public ImageButton getRightIconBtn() {
        return rightIconBtn;
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }
}