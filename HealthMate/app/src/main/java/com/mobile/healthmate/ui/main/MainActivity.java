package com.mobile.healthmate.ui.main;

import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.mobile.healthmate.BuildConfig;
import com.mobile.healthmate.R;
import com.mobile.healthmate.app.BaseActivity;
import com.mobile.healthmate.app.lib.viewinject.FindViewById;
import com.mobile.healthmate.app.lib.viewinject.ViewInjecter;
import com.mobile.healthmate.ui.main.view.TabItemView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements TabItemView.OnTabItemStateWillChangeDelegate {

    public final static String KEY_CLASS_NAME = "key_class_name";
    private static final long EXIT_TIME_INTERVAL = 2000;
    private long mBackPressedTime = System.currentTimeMillis();

    @FindViewById(R.id.tab_item_online)
    private TabItemView onlineItem;

    @FindViewById(R.id.tab_item_test)
    private TabItemView testItem;

    @FindViewById(R.id.tab_item_personal)
    private TabItemView personalItem;

    @FindViewById(R.id.tab_item_shop)
    private TabItemView shopItem;

    @FindViewById(R.id.tab_item_video)
    private TabItemView videoItem;

    /**
     * 当前的选中项
     */
    private List<TabItemView> tabs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_main);

        ViewInjecter.inject(this);

        // 设置每个tab的Fragment
        onlineItem.setFragmentClass(OnlineFragment.class);
        testItem.setFragmentClass(HealthTestFragment.class);
        personalItem.setFragmentClass(PersonalFragment.class);
        shopItem.setFragmentClass(ShopFragment.class);
        videoItem.setFragmentClass(VideoFragment.class);

        // 设置监听
        onlineItem.setDelegate(this);
        testItem.setDelegate(this);
        personalItem.setDelegate(this);
        shopItem.setDelegate(this);
        videoItem.setDelegate(this);

        tabs.add(onlineItem);
        tabs.add(testItem);
        tabs.add(personalItem);
        tabs.add(shopItem);
        tabs.add(videoItem);

        String className = getIntent().getStringExtra(KEY_CLASS_NAME);
        if (isStringEmpty(className)) {
            setIndexWithoutException(0);
        } else {
            changeIndexByClassName(className);
        }
    }

    /**
     * 选中某项标签
     */
    private void setIndexWithoutException(@IntRange(from = 0) int index) {
        if (BuildConfig.DEBUG) {
            if (index >= tabs.size() || index < 0) {
                throw new RuntimeException("index >= tabs.size() || index < 0, current index = " + index);
            }
        }
        try {
            setIndex(index);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isStringEmpty(String str) {
        return null == str || str.trim().equals("");
    }

    private void setIndex(int index) throws Exception {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (int i = 0; i < tabs.size(); i++) {
            TabItemView tab = tabs.get(i);
            Class<? extends Fragment> fragmentClass = tab.getFragmentClass();
            String tag = fragmentClass.getName();
            Fragment fragmentByTag = fragmentManager.findFragmentByTag(tag);

            if (i == index) {
                tab.setItemSelected(true);
                // 添加tab
                if (fragmentByTag == null) {
                    fragmentTransaction.add(R.id.container, fragmentClass.newInstance(), tag);
                } else {
                    if (fragmentByTag.isDetached()) {
                        fragmentTransaction.attach(fragmentByTag);
                    }
                }
            } else if (tab.isItemSelected()) {
                tab.setItemSelected(false);
                // 移除tab
                if (fragmentByTag != null) {
                    fragmentTransaction.detach(fragmentByTag);
                }
            }
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void changeIndexByClassName(String className) {
        for (int i = 0; i < tabs.size(); i++) {
            if (tabs.get(i).getFragmentClass().getSimpleName().equals(className)) {
                setIndexWithoutException(i);
                break;
            }
        }
    }


    @Override
    public boolean shouldChangeTabItemState(TabItemView tabItemView) {
        // 此方法用来判断是否需要登录
        // 未登录状态下，不能进入某些tab：

        if (tabItemView.isItemSelected()) {
            // 本身就是选中的，不改变
            return false;
        }
        return true;
    }

    @Override
    public void onTabItemStatChanged(TabItemView tabItemView) {
        int index = 0;
        for (int i = 0; i < tabs.size(); i++) {
            if (tabs.get(i) == tabItemView) {
                index = i;
                break;
            }
        }
        setIndexWithoutException(index);
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - mBackPressedTime <= EXIT_TIME_INTERVAL) {
            finish();
        } else {
            mBackPressedTime = currentTime;
            showToast(R.string.exit_application);
        }
    }
}
