package com.mobile.healthmate.app.lib.imageloader.downloader;

/**
 * 下载监听器
 *
 * @author zdxing 2015年1月26日
 */
public interface FileDownloadListener {
    /**
     * 进度回调
     *
     * @param totalSize   总的直接数，如果服务器没有返回该值，为-1
     * @param currentSize 当前已经下载的字节数
     */
    void onProgressChange(String filePath, int totalSize, int currentSize);

    /**
     * 下载结果回调
     */
    void onDownloadFinish(String filePath, DownloadResult downloadResult);

    /** 下载结果 */
    enum DownloadResult {
        /** */
        SUCCESSFUL,

        /** 下载失败 */
        FAILED,

        /** URL 为 null */
        URL_EMPTY,

        /** 文件已经存在 */
        FILE_EXISTS
    }
}
