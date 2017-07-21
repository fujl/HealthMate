package com.mobile.healthmate.app.lib.imageloader.cache;

import android.graphics.Bitmap;

import java.util.List;

/**
 * lruCache
 *
 * @author zdxing
 */
public interface BitmapLruCache {

    public void put(String key, Bitmap value);

    public Bitmap get(String key);

    public void remove(String key);

    public List<String> getAllKey();

    public void clear();

    public int getMaxSize();

    public int getCurrentSize();
}
