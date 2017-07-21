package com.mobile.healthmate.app.lib.imageloader.downloader;

import com.mobile.healthmate.app.lib.executor.UIThreadTask;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 下载任务
 *
 * @author zdxing 2015年1月26日
 */
class DownloadTask implements Runnable {
    public static final Object lock = new Object();
    private final Object cancelLock = new Object();
    private String filePath;
    private String url;
    private FileDownloadListener fileDownloadListener;
    private boolean isCancel = false;
    private boolean isRunning = false;

    public DownloadTask(String filePath, String url) {
        super();
        isCancel = false;
        this.filePath = filePath;
        this.url = url;
    }

    private static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    /**
     * 设置下载监听器
     */
    void setFileDownloadListener(FileDownloadListener fileDownloadListener) {
        this.fileDownloadListener = fileDownloadListener;
    }

    @Override
    public void run() {
        synchronized (cancelLock) {
            if (isCancel) {
                return;
            } else {
                isRunning = true;
            }
        }
        File file = new File(filePath);
        FileDownloadListener.DownloadResult downloadResult;
        if (file.exists()) {
            downloadResult = FileDownloadListener.DownloadResult.FILE_EXISTS;
        } else {
            if (url == null || url.trim().equals("")) {
                downloadResult = FileDownloadListener.DownloadResult.URL_EMPTY;
            } else {
                OutputStream outputStream = null;
                HttpURLConnection httpURLConnection = null;
                try {
                    httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setReadTimeout(5000);
                    httpURLConnection.setRequestMethod("GET");
                    int responseCode = httpURLConnection.getResponseCode();
                    if (responseCode == 200) {
                        InputStream inputStream = httpURLConnection.getInputStream();

                        synchronized (lock) {
                            file.getParentFile().mkdirs();
                            file.createNewFile();
                        }
                        outputStream = new FileOutputStream(file);

                        byte[] buffer = new byte[1024];
                        int length = -1;
                        int totalSize = httpURLConnection.getContentLength();
                        int currentSize = 0;
                        long lastNotify = System.currentTimeMillis();

                        while ((length = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, length);
                            currentSize += length;
                            if ((System.currentTimeMillis() - lastNotify) > 200) {
                                new UiTaskProcess(totalSize, currentSize).execute();
                                lastNotify = System.currentTimeMillis();
                            }
                        }
                        outputStream.flush();
                        new UiTaskProcess(totalSize, currentSize).execute();
                        downloadResult = FileDownloadListener.DownloadResult.SUCCESSFUL;
                    } else {
                        file.deleteOnExit();
                        downloadResult = FileDownloadListener.DownloadResult.FAILED;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (outputStream != null)
                        try {
                            outputStream.flush();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    file.delete();
                    downloadResult = FileDownloadListener.DownloadResult.FAILED;
                } finally {
                    closeQuietly(outputStream);
                    if (httpURLConnection != null)
                        httpURLConnection.disconnect();
                }
            }
        }
        new UiTaskResult(downloadResult).execute();
    }

    /**
     * 取消下载： 取消还未开始的下载，如果已经开始，则不能取消
     *
     * @return true：取消成功，将不会开始下载
     */
    public boolean cancel() {
        boolean result;
        synchronized (cancelLock) {
            if (!isCancel && !isRunning) {
                result = true;
                isCancel = true;
            } else {
                result = false;
            }
        }
        return result;
    }

    private class UiTaskProcess extends UIThreadTask {
        int totalSize;
        int currentSize;

        public UiTaskProcess(int totalSize, int currentSize) {
            super();
            this.totalSize = totalSize;
            this.currentSize = currentSize;
        }

        @Override
        protected void runOnUIThread() {
            if (fileDownloadListener != null)
                fileDownloadListener.onProgressChange(filePath, totalSize, currentSize);
        }
    }

    private class UiTaskResult extends UIThreadTask {
        FileDownloadListener.DownloadResult downloadResult;

        public UiTaskResult(FileDownloadListener.DownloadResult downloadResult) {
            super();
            this.downloadResult = downloadResult;
        }

        @Override
        protected void runOnUIThread() {
            if (fileDownloadListener != null)
                fileDownloadListener.onDownloadFinish(filePath, downloadResult);
        }
    }
}
