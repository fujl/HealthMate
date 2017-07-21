package com.mobile.healthmate.app.lib.imageloader.downloader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 下载器
 *
 * @author zdxing 2015年1月26日
 */
public class ImageDownloader {
    private ExecutorService executorService;

    private Map<String, Entry> tasks = new HashMap<>();

    public ImageDownloader(int downloadThreadSize) {
        executorService = Executors.newFixedThreadPool(downloadThreadSize);
    }

    public ImageDownLoadTask download(String filePath, String url, FileDownloadListener imageDownloadListener) {
        Entry entry = tasks.get(filePath);
        if (entry == null) {
            DownloadTask downloadTask = new DownloadTask(filePath, url);
            entry = new Entry(filePath, downloadTask);
            tasks.put(filePath, entry);
            executorService.execute(downloadTask);
        }
        entry.add(imageDownloadListener);

        return new ImageDownLoadTask(entry, imageDownloadListener);
    }

    /**
     * 是否正在下载
     *
     * @param filePath 文件的本地保存路径
     * @return true:当前文件正在下载
     */
    public boolean isDownloading(String filePath) {
        return tasks.containsKey(filePath);
    }

    /**
     * 下载任务
     *
     * @author zdxing 2015年1月26日
     */
    public class ImageDownLoadTask {
        private Entry entry;
        private FileDownloadListener imageDownloadListener;

        public ImageDownLoadTask(Entry entry, FileDownloadListener imageDownloadListener) {
            super();
            this.entry = entry;
            this.imageDownloadListener = imageDownloadListener;
        }

        /**
         * 取消下载，注意：只会取消未开始的下载，正在下载的任务不会被取消
         */
        public void cancel() {
            entry.remove(imageDownloadListener);
        }
    }

    public class Entry implements FileDownloadListener {
        DownloadTask downloadTask;
        List<FileDownloadListener> imageDownloadListeners = new ArrayList<>();
        private String localFilePath;

        public Entry(String filePath, DownloadTask downloadTask) {
            this.localFilePath = filePath;
            this.downloadTask = downloadTask;
            this.downloadTask.setFileDownloadListener(this);
        }

        public void remove(FileDownloadListener imageDownloadListener) {
            if (imageDownloadListeners.remove(imageDownloadListener)) {

                if (imageDownloadListeners.size() == 0 && downloadTask.cancel()) {
                    // 取消未开始的任务成功
                    tasks.remove(localFilePath);
                }
            }
        }

        public void add(FileDownloadListener imageDownloadListener) {
            imageDownloadListeners.add(imageDownloadListener);
        }

        @Override
        public void onProgressChange(String filePath, int totalSize, int currentSize) {
            for (FileDownloadListener imageDownloadListener : imageDownloadListeners) {
                imageDownloadListener.onProgressChange(filePath, totalSize, currentSize);
            }
        }

        @Override
        public void onDownloadFinish(String filePath, DownloadResult downloadResult) {
            for (FileDownloadListener imageDownloadListener : imageDownloadListeners) {
                imageDownloadListener.onDownloadFinish(filePath, downloadResult);
            }
            tasks.remove(localFilePath);
        }
    }
}