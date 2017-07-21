package com.mobile.healthmate.app.lib.imageloader;

/**
 * 进度监听
 *
 * @author zdxing 2015年2月4日
 */
public interface OnLoadProgressChangeListener {
    /**
     * 进度监听
     *
     * @param totalSize   总大小
     * @param currentSize 当前大小
     */
    public void onLoadProgressChange(int totalSize, int currentSize);
}
