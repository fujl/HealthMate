package com.mobile.healthmate.view.listview;


/**
 * 下拉的监听接口
 *
 * @author zengdexing
 */
public interface OnPullRefreshListener {
    /**
     * 下拉刷新时调用
     *
     * @param pullRefreshView
     */
    public void onPullDownRefresh(PullRefreshView pullRefreshView);
}
