package com.mobile.healthmate.manager.user;

import com.mobile.healthmate.app.App;

/**
 * 用户信息变化监听
 * <p>Created by zdxing on 2015-07-07.</p>
 */
public interface OnUserChangeListener {
    /**
     * 用户登录
     *
     * @param userInfo 登录的用户信息
     */
    void onUserDidLogin(LoginInfo userInfo);

    void onUserWillLogin(LoginInfo userInfo);

    /**
     * 用户将要退出登录
     */
    void onUserWillLogout(OnUserChangeFilter filter);

    /**
     * 用户注销了
     *
     * @param userInfo 注销的用户信息
     */
    void onUserDidLogout(LoginInfo userInfo);

    interface OnUserChangeFilter {
        void doNext();
    }

    class SimpleOnUserChangeListener implements OnUserChangeListener {

        @Override
        public void onUserDidLogin(LoginInfo userInfo) {

        }

        @Override
        public void onUserWillLogin(LoginInfo userInfo) {
        }

        @Override
        public void onUserWillLogout(final OnUserChangeFilter filter) {
            App.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 接着干下一件事情
                    filter.doNext();
                }
            }, 100);
        }

        @Override
        public void onUserDidLogout(LoginInfo userInfo) {

        }
    }
}
