package com.mobile.healthmate.app.lib.imageloader.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.mobile.healthmate.R;
import com.mobile.healthmate.app.App;
import com.mobile.healthmate.app.lib.imageloader.ImageLoadListener;
import com.mobile.healthmate.app.lib.imageloader.ImageLoadOptions;
import com.mobile.healthmate.app.lib.imageloader.ImageLoadSize;
import com.mobile.healthmate.app.lib.imageloader.ImageLoadTask;
import com.mobile.healthmate.app.lib.imageloader.ImageLoader;
import com.mobile.healthmate.app.lib.imageloader.ImageScaleType;
import com.mobile.healthmate.manager.SdManager;

/**
 * 异步图片加载控件
 *
 * @author zdxing 2015年1月30日
 */
public class AsyncImageView extends SimpleImageView {
    private ImageLoader imageLoader;

    /** 图片加载任务 */
    private ImageLoadTask imageLoadTask;

    /** 图片加载网络路径 */
    private String url;

    /** 图片加载本地路径 */
    private String filePath;

    /** 加载中显示的图片 */
    private Drawable loadingDrawable;

    /** 图片加载失败显示的图片 */
    private Drawable loadFailedDrawable;

    /** 是否使用内存缓存 */
    private boolean isMemoryCacheEnable = true;

    /** 是否使用缩略图缓存 */
    private boolean isDiskCacheEnable = false;

    /** 图片处理 */
    private ImageProcessor imageProcessor;

    /** 图片监听 */
    private ImageLoadListener imgloadListener;

    /**
     * 图片加载监听器
     */
    private ImageLoadListener imageLoadListener = new ImageLoadListener() {
        @Override
        public void onLoadFailed(String reason) {
            AsyncImageView.super.setImageDrawable(loadFailedDrawable);
            if (imgloadListener != null) {
                imgloadListener.onLoadFailed(reason);
            }
        }

        @Override
        public void onLoadProgressChange(int totalSize, int currentSize) {
            if (imgloadListener != null) {
                imgloadListener.onLoadProgressChange(totalSize, currentSize);
            }
        }

        @Override
        public void onLoadSuccessful(final BitmapSource bitmapSource, Bitmap bitmap) {
            Drawable drawable;
            if (imageProcessor != null) {
                drawable = imageProcessor.onProcessImage(bitmapSource, bitmap);
            } else {
                drawable = new BitmapDrawable(getResources(), bitmap);
            }
            AsyncImageView.super.setImageDrawable(drawable);
            if (imgloadListener != null) {
                imgloadListener.onLoadSuccessful(bitmapSource, bitmap);
            }
        }

        @Override
        public void onStartDownload() {
            if (imgloadListener != null) {
                imgloadListener.onStartDownload();
            }
        }
    };

    public AsyncImageView(Context context) {
        this(context, null);
    }

    public AsyncImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AsyncImageView);
        setDiskCacheEnable(typedArray.getBoolean(R.styleable.AsyncImageView_isDiskCacheEnable, false));
        setMemoryCacheEnable(typedArray.getBoolean(R.styleable.AsyncImageView_isMemoryCacheEnable, true));
        setImageLoadingDrawable(typedArray.getDrawable(R.styleable.AsyncImageView_loadingDrawable));
        setImageLoadFailedDrawable(typedArray.getDrawable(R.styleable.AsyncImageView_loadFailedDrawable));

        int imageShape = typedArray.getInt(R.styleable.AsyncImageView_imageShape, 2);
        final int cornerRadius = typedArray.getInt(R.styleable.AsyncImageView_cornerRadius, -1);
        if (imageShape == 0) {
            setImageProcessor(new ImageProcessor() {
                @Override
                public Drawable onProcessImage(ImageLoadListener.BitmapSource bitmapSource, Bitmap bitmap) {
                    return new CircledDrawable(bitmap);
                }
            });
        } else if (imageShape == 1) {
            setImageProcessor(new ImageProcessor() {
                @Override
                public Drawable onProcessImage(ImageLoadListener.BitmapSource bitmapSource, Bitmap bitmap) {
                    int tempCorner = cornerRadius;
                    if (tempCorner == -1) {
                        tempCorner = Math.min(bitmap.getWidth() / 8, bitmap.getHeight() / 8);
                    }
                    return new RoundedDrawable(bitmap, tempCorner, tempCorner, 0);
                }
            });
        }

        typedArray.recycle();
        if (isInEditMode()) {
            return;
        }
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (filePath != null) {
            loadImage(filePath, url);
        }
    }

    @Override
    public void setImageScaleType(ImageScaleType imageScaleType) {
        super.setImageScaleType(imageScaleType);
        if (filePath != null) {
            loadImage(filePath, url);
        }
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        if (imageLoadTask != null) {
            imageLoadTask.cancle();
        }

        this.filePath = null;
        this.url = null;

        super.setImageDrawable(drawable);
    }

    /**
     * 开始异步加载图片
     *
     * @param filePath 图片路径
     * @param url      图片url地址，http, 不可为null
     */
    public void loadImage(String filePath, String url) {
        if (imageLoadTask != null) {
            imageLoadTask.cancle();
        }

        this.filePath = filePath;
        this.url = url;

        if (filePath == null) {
            super.setImageDrawable(null);
        } else {
            int width = getWidth() - getPaddingLeft() - getPaddingRight();
            int height = getHeight() - getPaddingBottom() - getPaddingTop();

            if (width > 0 && height > 0) {
                super.setImageDrawable(loadingDrawable);

                ImageLoadOptions.Builder builder = new ImageLoadOptions.Builder(filePath);
                builder.setImageLoadSize(new ImageLoadSize(width, height, getImageScaleType()));
                builder.setUrl(url);
                builder.setMemoryCacheEnable(isMemoryCacheEnable);
                builder.setDiskCacheEnable(isDiskCacheEnable);
                imageLoadTask = imageLoader.loadImage(builder.build(), imageLoadListener);
            }
        }
    }

    /**
     * 加载网络图片
     *
     * @param url 网络图片地址
     */
    public void loadUrlImage(@NonNull String url) {
        String filePath = App.getInstance().getManager(SdManager.class).getImagePath(url);
        loadImage(filePath, url);
    }

    /**
     * 加载本地图片
     * @param filePath
     */
    public void loadLocalImage(String filePath) {
        loadImage(filePath, "");
    }

    /**
     * 设置加载中显示的图片
     *
     * @param loadingDrawable 加载中显示的图片，可以为null
     */
    public void setImageLoadingDrawable(Drawable loadingDrawable) {
        this.loadingDrawable = loadingDrawable;
        if (filePath != null && imageLoadTask != null) {
            ImageLoadTask.LoadState loadState = imageLoadTask.getLoadState();
            if (loadState != ImageLoadTask.LoadState.加载取消 && loadState != ImageLoadTask.LoadState.加载完成 && loadState != ImageLoadTask.LoadState.加载失败) {
                // 正在加载中
                super.setImageDrawable(loadingDrawable);
            }
        }
    }

    /**
     * 设置加载失败显示的图片
     *
     * @param loadFailedDrawable 加载失败时显示的图片，可以为null
     */
    public void setImageLoadFailedDrawable(Drawable loadFailedDrawable) {
        this.loadFailedDrawable = loadFailedDrawable;
        if (filePath != null && imageLoadTask != null) {
            ImageLoadTask.LoadState loadState = imageLoadTask.getLoadState();
            if (loadState == ImageLoadTask.LoadState.加载失败) {
                super.setImageDrawable(loadFailedDrawable);
            }
        }
    }

    /**
     * 加载是否使用内存缓存，如果内存中有缓存图片，将直接使用内存缓存
     */
    public boolean isMemoryCacheEnable() {
        return isMemoryCacheEnable;
    }

    /**
     * 加载是否使用内存缓存，默认true, 如果设置false，则每次都会加载文件
     */
    public void setMemoryCacheEnable(boolean isMemoryCacheEnable) {
        this.isMemoryCacheEnable = isMemoryCacheEnable;
    }

    /**
     * 加载是否使用缩略图缓存
     *
     * @return true：加载使用缩略图缓存
     */
    public boolean isDiskCacheEnable() {
        return isDiskCacheEnable;
    }

    /**
     * 设置加载中是否使用缩略图缓存,默认false<br />
     * 加载jpg时，设置为true，将大大提升加载速度。<br />
     * 如果加载png图片，建议设置该值为false，否则透明部分会丢失，图片变成黑色
     *
     * @param isDiskCacheEnable 如果设置为false，将不使用缩略图缓存，直接加载图片原图
     */
    public void setDiskCacheEnable(boolean isDiskCacheEnable) {
        this.isDiskCacheEnable = isDiskCacheEnable;
    }

    /**
     * 设置图片处理器，用于包装bitmap
     */
    public void setImageProcessor(ImageProcessor imageProcessor) {
        this.imageProcessor = imageProcessor;
    }

    /**
     * 设置图片加载监听器
     *
     * @param imageLoadListener 图片加载监听
     */
    public void setImageLoadListener(ImageLoadListener imageLoadListener) {
        this.imgloadListener = imageLoadListener;
    }
}
