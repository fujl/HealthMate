package com.mobile.healthmate.view.listview;

/**
 * 加载更多监听器
 *
 * @author zengdexing
 */
public interface OnLoadMoreListener {
    /**
     * 产生加载更多事件的回调
     *
     * @param pullRefreshView
     */
    public void onLoadMore(PullRefreshView pullRefreshView);
}
