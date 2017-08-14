package com.mobile.healthmate.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.StringRes;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobile.healthmate.R;
import com.mobile.healthmate.app.lib.listener.SimpleTextWatcher;
import com.mobile.healthmate.app.lib.viewinject.FindViewById;
import com.mobile.healthmate.app.lib.viewinject.ViewInjecter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 通用数据显示视图（标题+内容）
 * Created by wangyu on 2017/1/5.
 */

public class CommonItemView extends RelativeLayout {

    private boolean shouldCheckSelf = false;

    @FindViewById(R.id.all_layout)
    private RelativeLayout relativeLayout;

    /** 标题 */
    @FindViewById(R.id.common_info_item_title)
    private TextView mTitle;

    /** 左图标 */
    @FindViewById(R.id.common_info_item_left_icon)
    private ImageView mLeftIcon;

    /** 显示内容 */
    @FindViewById(R.id.common_info_item_content)
    private TextView mContent;

    /** 输入内容 */
    @FindViewById(R.id.common_info_item_input_content)
    private EditText mInput;

    /** 输入框提示语 */
    @FindViewById(R.id.common_info_item_input_hint_txt)
    private TextView mInputHintTxt;

    /** 底线 */
    @FindViewById(R.id.common_info_item_bottom_line)
    private View mBottomLine;

    /** 顶线 */
    @FindViewById(R.id.common_info_item_top_line)
    private View mTopLine;

    private Context mContext;

    /** 视图模式（显示|输入） */
    private CommonInfoViewType viewType = CommonInfoViewType.SHOW;

    private SimpleTextWatcher onInputTextChangeListener = new SimpleTextWatcher(){

        @Override
        public void afterTextChanged(Editable s) {
            super.afterTextChanged(s);
            if (getCommonViewType() == CommonInfoViewType.INPUT) {
                if (mInput.getText().length() == 0) {
                    mInputHintTxt.setVisibility(VISIBLE);
                } else {
                    mInputHintTxt.setVisibility(GONE);
                }
            }
        }
    };

    public CommonItemView(Context context) {
        this(context, null, 0);
    }

    public CommonItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initAttrs(context, attrs, defStyleAttr);
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CommonItemView, defStyleAttr, 0);
        int n = typedArray.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.CommonItemView_commonItemTitle) {
                String title = typedArray.getString(attr);
                setTitle(title);
            } else if (attr == R.styleable.CommonItemView_commonItemContent) {
                String content = typedArray.getString(attr);
                setContent(content);
            } else if (attr == R.styleable.CommonItemView_commonItemContentLeftIcon) {
                Drawable drawableLeft = typedArray.getDrawable(attr);
                setContentLeftDrawable(drawableLeft);
            } else if (attr == R.styleable.CommonItemView_commonItemContentRightIcon) {
                Drawable drawableRight = typedArray.getDrawable(attr);
                setContentRightDrawable(drawableRight);
            } else if (attr == R.styleable.CommonItemView_commonItemType) {
                CommonInfoViewType viewType = CommonInfoViewType.valueOf(typedArray.getInt(attr, 1));
                setCommonViewType(viewType);
            } else if (attr == R.styleable.CommonItemView_commonItemContentHint) {
                String hint = typedArray.getString(attr);
                setContentHint(hint);
            } else if (attr == R.styleable.CommonItemView_commonItemSimpleImeOption) {
                CommonSimpleInputIme inputIme = CommonSimpleInputIme.valueOf(typedArray.getInt(attr, 1));
                setInputSimpleImeOption(inputIme);
            } else if (attr == R.styleable.CommonItemView_commonItemSimpleInputType) {
                CommonSimpleInputType inputType = CommonSimpleInputType.valueOf(typedArray.getInt(attr, 2));
                setInputSimpleInputType(inputType);
            } else if (attr == R.styleable.CommonItemView_commonItemLineState) {
                CommonLineState lineState = CommonLineState.valueOf(typedArray.getInt(attr, 1));
                setLineState(lineState);
            } else if (attr == R.styleable.CommonItemView_commonItemInputMaxLength) {
                setMaxInputLength(typedArray.getInt(attr, 10000));
            } else if (attr == R.styleable.CommonItemView_commonItemTitleColor) {
                setTitleColor(typedArray.getColor(attr, Color.parseColor("#444444")));
            } else if (attr == R.styleable.CommonItemView_commonItemContentColor) {
                setContentColor(typedArray.getColor(attr, Color.parseColor("#808080")));
            } else if (attr == R.styleable.CommonItemView_commonItemTitleTextSize) {
                float size = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                setTitleTextSize(size);
            } else if (attr == R.styleable.CommonItemView_commonItemContentTextSize) {
                float size = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
                setContentTextSize(size);
            }
        }

        typedArray.recycle();
    }

    private void initView(Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.view_common_info_item, this, false);
        ViewInjecter.inject(this, view);
        this.addView(view);
        setViewOpType();
        mInput.addTextChangedListener(onInputTextChangeListener);
        setLineState(CommonLineState.ONLY_BOTTOM);
        setInputSimpleInputType(CommonSimpleInputType.TYPE_CLASS_TEXT);
    }

    /**
     * 数据校验
     * @return
     */
    public boolean checkSelf() {
        return !isShouldCheckSelf() || doCheckData();
    }

    public boolean doCheckData() {
        if (TextUtils.isEmpty(getContentString())) {
            onContentException();
            return false;
        } else {
            onContentRightful();
            return true;
        }
    }

    public boolean isShouldCheckSelf() {
        return shouldCheckSelf;
    }

    public void setShouldCheckSelf(boolean shouldCheckSelf) {
        this.shouldCheckSelf = shouldCheckSelf;
    }

    /**
     * 设置标题
     * @param titleResId  标题Rid
     */
    public void setTitle(@StringRes int titleResId) {
        setTitle(getResources().getString(titleResId));
    }

    /**
     * 设置标题
     * @param title  标题
     */
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    /**
     * 获取标题
     * @return  标题
     */
    public String getTitle() {
        return mTitle.getText().toString().trim();
    }

    /**
     * 设置标题字体大小
     * @param size
     */
    public void setTitleTextSize(float size) {
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    /**
     * 内容异常时标记title颜色
     */
    public void onContentException() {
        setTitleColor(Color.RED);
    }

    /**
     * 内容正常时恢复title颜色
     */
    public void onContentRightful() {
        setTitleColor(getResources().getColor(R.color.textTitle));
    }

    /**
     * 设置标题颜色
     * @param color 文本颜色
     */
    public void setTitleColor(int color) {
        mTitle.setTextColor(color);
    }

    /**
     * 设置内容
     * @param contentResId  内容Rid
     */
    public void setContent(int contentResId) {
        setContent(getResources().getString(contentResId));
    }

    /**
     * 设置内容
     * @param content  内容
     */
    public void setContent(String content) {
        if (TextUtils.isEmpty(content)) {
            content = "";
        }
        setContent(content, true);
    }

    /**
     * 设置内容
     * @param content   内容
     * @param enable    enable
     */
    public void setContent(String content, boolean enable) {
        if (viewType == CommonInfoViewType.INPUT) {
            mInput.setText(content);
            mInput.setEnabled(enable);
        } else {
            mContent.setText(content);
            mContent.setEnabled(enable);
        }
        this.setEnabled(enable);
    }

    /**
     * 设置内容字体大小
     * @param size
     */
    public void setContentTextSize(float size) {
        mContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }
    /**
     * 设置内容文本颜色
     * @param color  文本颜色
     */
    public void setContentColor(int color) {
        mContent.setTextColor(color);
    }

    /**
     * 设置输入框hint
     * @param resId hintId
     */
    public void setContentHint(int resId) {
        setContentHint(getResources().getString(resId));
    }

    /**
     * 设置输入框hint
     * @param hint hint
     */
    public void setContentHint(String hint) {
        mInputHintTxt.setHint(hint);
        mContent.setHint(hint);
    }

    /**
     * 获取内容
     * @return 内容字符串
     */
    public String getContentString() {
        String result = "";
        switch (viewType) {
            case SHOW:
                result = mContent.getText().toString().trim();
                break;
            case INPUT:
                result = mInput.getText().toString().trim();
                break;
            default:
                break;
        }
        return TextUtils.isEmpty(result) ? "" : result;
    }

    /**
     * 设置内容左图标
     * @param drawableResId  图标Rid
     */
    public void setContentLeftDrawable(int drawableResId) {
        Drawable drawable = getResources().getDrawable(drawableResId);
        setContentLeftDrawable(drawable);
    }

    /**
     * 设置内容左图标
     * @param drawable  图标
     */
    public void setContentLeftDrawable(Drawable drawable) {
        if (drawable == null) {
            return;
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        Drawable[] drawables = mContent.getCompoundDrawables();
        mContent.setCompoundDrawables(drawable, drawables[1], drawables[2], drawables[3]);
    }

    /**
     * 设置内容右图标
     * @param drawableResId  图标Rid
     */
    public void setContentRightDrawable(int drawableResId) {
        Drawable drawable = getResources().getDrawable(drawableResId);
        setContentRightDrawable(drawable);
    }

    /**
     * 设置内容右图标
     * @param drawable  图标
     */
    public void setContentRightDrawable(Drawable drawable) {
        if (drawable == null) {
            return;
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        Drawable[] drawables = mContent.getCompoundDrawables();
        mContent.setCompoundDrawables(drawables[0], drawables[1], drawable, drawables[3]);
    }

    /**
     * 设置标题点击事件
     * @param onClickListener  标题点击事件
     */
    public void setOnTitleClickListener(View.OnClickListener onClickListener) {
        mTitle.setOnClickListener(onClickListener);
    }

    /**
     * 设置左图标点击事件
     * @param onClickListener  图标点击事件
     */
    public void setOnLeftIconClickListener(View.OnClickListener onClickListener) {
        mLeftIcon.setOnClickListener(onClickListener);
    }

    /**
     * 设置内容点击事件
     * @param onClickListener  内容点击事件
     */
    public void setOnContentClickListener(View.OnClickListener onClickListener) {
        mContent.setOnClickListener(onClickListener);
    }

    /**
     * 设置输入框按键处理事件
     * @param onKeyListener  输入框按键处理事件
     */
    public void setOnInputKeyListener(OnKeyListener onKeyListener) {
        mInput.setOnKeyListener(onKeyListener);
    }

    /**
     * 获取视图显示类型（显示或者输入）
     * @return viewType
     */
    public CommonInfoViewType getCommonViewType() {
        return viewType;
    }

    /**
     * 设置视图显示类型
     * @param viewType viewType
     */
    public void setCommonViewType(CommonInfoViewType viewType) {
        this.viewType = viewType;
        setViewOpType();
    }

    /**
     * 获取输入框对象
     * @return editText
     */
    public EditText getInputEditText() {
        return mInput;
    }

    /**
     * 修改控件状态
     */
    private void setViewOpType() {
        switch (viewType) {
            case SHOW:
                mContent.setVisibility(VISIBLE);
                mInput.setVisibility(GONE);
                mInput.setText("");
                mInputHintTxt.setVisibility(GONE);
                break;
            case INPUT:
                mInput.setVisibility(VISIBLE);
                mInputHintTxt.setVisibility(VISIBLE);
                mContent.setVisibility(GONE);
                mContent.setText("");
        }
    }

    /**
     * 设置输入框imeOption
     * @param simpleInputIme CommonSimpleInputIme
     */
    public void setInputSimpleImeOption(CommonSimpleInputIme simpleInputIme) {
        switch (simpleInputIme) {
            case IME_ACTION_DONE:
                mInput.setImeOptions(EditorInfo.IME_ACTION_DONE);
                break;
            case IME_ACTION_GO:
                mInput.setImeOptions(EditorInfo.IME_ACTION_GO);
                break;
            case IME_ACTION_NEXT:
                mInput.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                break;
            case IME_ACTION_NONE:
                mInput.setImeOptions(EditorInfo.IME_ACTION_NONE);
                break;
            case IME_ACTION_PREVIOUS:
                mInput.setImeOptions(EditorInfo.IME_ACTION_PREVIOUS);
                break;
            case IME_ACTION_SEARCH:
                mInput.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
                break;
            case IME_ACTION_SEND:
                mInput.setImeOptions(EditorInfo.IME_ACTION_SEND);
                break;
            case IME_ACTION_UNSPECIFIED:
                mInput.setImeOptions(EditorInfo.IME_ACTION_UNSPECIFIED);
                break;
            default:
                mInput.setImeOptions(EditorInfo.IME_ACTION_DONE);
                break;
        }
    }

    /**
     * 设置输入框输入类型
     * @param simpleInputType CommonSimpleInputType
     */
    public void setInputSimpleInputType(CommonSimpleInputType simpleInputType) {
        switch (simpleInputType) {
            case TYPE_NULL:
                mInput.setInputType(InputType.TYPE_NULL);
                break;
            case TYPE_CLASS_TEXT:
                mInput.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case TYPE_CLASS_NUMBER:
                mInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case TYPE_PASSWORD:
                mInput.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case TYPE_PASSWORD_VISIBLE:
                mInput.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                break;
            case TYPE_DATE:
                mInput.setInputType(InputType.TYPE_CLASS_DATETIME);
                break;
            case TYPE_TEXT_MULTI_LINE:
                mInput.setInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
                mInput.setSingleLine(false);
                mInput.setHorizontallyScrolling(false);
                break;
            case TYPE_NUMBER_DECIMAL:
                mInput.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                break;
            default:
                mInput.setInputType(InputType.TYPE_NULL);
                break;
        }
    }

    /**
     * 设置线条状态
     * @param commonLineState  线条状态
     */
    public void setLineState(CommonLineState commonLineState) {
        switch (commonLineState) {
            case ONLY_BOTTOM:
                mBottomLine.setVisibility(View.VISIBLE);
                mTopLine.setVisibility(INVISIBLE);
                break;
            case ONLY_TOP:
                mTopLine.setVisibility(VISIBLE);
                mBottomLine.setVisibility(View.INVISIBLE);
                break;
            case BOTH_AND:
                mTopLine.setVisibility(VISIBLE);
                mBottomLine.setVisibility(VISIBLE);
                break;
            case NEITHER_NOR:
                mTopLine.setVisibility(INVISIBLE);
                mBottomLine.setVisibility(INVISIBLE);
        }
    }

    /**
     * 设置输入框内容长度限制
     * @param length length
     */
    public void setMaxInputLength(int length) {
        InputFilter lengthFilter = null;
        /* asList返回值是AbstractList, 默认不执行add,remove等操作 */
        List<InputFilter> filterList = new ArrayList<>(Arrays.asList(mInput.getFilters()));
        for (InputFilter filter : filterList) {
            if (filter instanceof InputFilter.LengthFilter) {
                lengthFilter = filter;//找到长度限制过滤器
                break;
            }
        }
        if (lengthFilter != null) {//存在长度限制过滤器
            filterList.remove(lengthFilter);//移除
        }
        filterList.add(new InputFilter.LengthFilter(length));//增加新的过滤器
        mInput.setFilters(filterList.toArray(new InputFilter[filterList.size()]));//重新设置过滤器
    }

    /**
     * 增加输入框输入过滤器
     * 可能有重复的过滤器被添加的情况，需要注意
     * @param filter 过滤器
     */
    public void addInputFilterToFilters(InputFilter filter) {
        List<InputFilter> inputFilterList = new ArrayList<>();
        List<InputFilter> srcList = Arrays.asList(mInput.getFilters());
        inputFilterList.addAll(srcList);
        inputFilterList.add(filter);
        InputFilter[] totalFilters = new InputFilter[inputFilterList.size()];
        mInput.setFilters(inputFilterList.toArray(totalFilters));
    }

    /**
     * 清除所有过滤器
     */
    public void clearInputFilter() {
        mInput.setFilters(new InputFilter[]{});
    }

    public TextView getContentView(){
        return mContent;
    }

    /**
     * 视图模式枚举类
     */
    public enum CommonInfoViewType {
        SHOW(1), INPUT(2);

        private int value = 1;

        CommonInfoViewType(int value) {
            this.value = value;
        }

        public static CommonInfoViewType valueOf(int value) {
            switch (value) {
                case 1:
                    return SHOW;
                case 2:
                    return INPUT;
                default:
                    return SHOW;
            }
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 输入框输入类型（提供简单的xml配置，更多InputType如TYPE_TEXT_FLAG_MULTI_LINE等类型请获取mInput对象使用代码进行设置）
     */
    public enum CommonSimpleInputType {
        TYPE_NULL(1),
        TYPE_CLASS_TEXT(2),
        TYPE_CLASS_NUMBER(3),
        TYPE_PASSWORD(4),
        TYPE_PASSWORD_VISIBLE(5),
        TYPE_DATE(6),
        TYPE_TEXT_MULTI_LINE(7),
        TYPE_NUMBER_DECIMAL(8);

        private int value = 1;

        CommonSimpleInputType(int value) {
            this.value = value;
        }

        public static CommonSimpleInputType valueOf(int value) {
            switch (value) {
                case 1:
                    return TYPE_NULL;
                case 2:
                    return TYPE_CLASS_TEXT;
                case 3:
                    return TYPE_CLASS_NUMBER;
                case 4:
                    return TYPE_PASSWORD;
                case 5:
                    return TYPE_PASSWORD_VISIBLE;
                case 6:
                    return TYPE_DATE;
                case 7:
                    return TYPE_TEXT_MULTI_LINE;
                case 8:
                    return TYPE_NUMBER_DECIMAL;
                default:
                    return TYPE_NULL;
            }
        }
        public int getValue() {
            return value;
        }
    }

    /**
     * 输入框键盘按钮行为（提供简单的xml配置，更多如IME_FLAG_NO_FULLSCREEN等类型请获取mInput对象使用代码进行设置）
     */
    public enum CommonSimpleInputIme {
        IME_ACTION_DONE(1),
        IME_ACTION_GO(2),
        IME_ACTION_NEXT(3),
        IME_ACTION_NONE(4),
        IME_ACTION_PREVIOUS(5),
        IME_ACTION_SEARCH(6),
        IME_ACTION_SEND(7),
        IME_ACTION_UNSPECIFIED(8);

        private int value = 1;

        CommonSimpleInputIme(int value) {
            this.value = value;
        }
        public static CommonSimpleInputIme valueOf(int value) {
            switch (value) {
                case 1:
                    return IME_ACTION_DONE;
                case 2:
                    return IME_ACTION_GO;
                case 3:
                    return IME_ACTION_NEXT;
                case 4:
                    return IME_ACTION_NONE;
                case 5:
                    return IME_ACTION_PREVIOUS;
                case 6:
                    return IME_ACTION_SEARCH;
                case 7:
                    return IME_ACTION_SEND;
                case 8:
                    return IME_ACTION_UNSPECIFIED;
                default:
                    return IME_ACTION_DONE;
            }
        }
        public int getValue() {
            return value;
        }
    }

    /**
     * 线条状态枚举类
     */
    public enum CommonLineState {
        ONLY_BOTTOM(1), ONLY_TOP(2), BOTH_AND(3), NEITHER_NOR(4);

        private int value = 1;
        CommonLineState(int value) {
            this.value = value;
        }

        public static CommonLineState valueOf(int value) {
            switch (value) {
                case 1:
                    return ONLY_BOTTOM;
                case 2:
                    return ONLY_TOP;
                case 3:
                    return BOTH_AND;
                case 4:
                    return NEITHER_NOR;
                default:
                    return ONLY_BOTTOM;
            }
        }

        public int getValue() {
            return value;
        }
    }
}
