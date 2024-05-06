package com.example.myapplication;

import android.content.Context;

import androidx.annotation.OptIn;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DefaultDataSource;
import androidx.media3.datasource.cache.Cache;
import androidx.media3.datasource.cache.CacheDataSource;
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor;
import androidx.media3.datasource.cache.SimpleCache;

import java.io.File;

public class CacheUtil {
    private static final String CACHE_DIRECTORY ="exoplayer_Cache";
    private static final long CACHE_SIZE = 90*1024*1024;



    @OptIn(markerClass = UnstableApi.class) public static Cache getCache(Context context) {
        File cacheDirectory = new File(context.getApplicationContext().getCacheDir(), CACHE_DIRECTORY);
        return new SimpleCache(cacheDirectory, new LeastRecentlyUsedCacheEvictor(CACHE_SIZE));
    }
    @OptIn(markerClass = UnstableApi.class) public static CacheDataSource.Factory getCacheDataSourceFactory(Context context) {
        Cache cache = getCache(context);
        return new CacheDataSource.Factory()
                .setCache(cache)
                .setUpstreamDataSourceFactory(new DefaultDataSource.Factory(context))
                .setCacheWriteDataSinkFactory(null)
                .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);
    }
    @OptIn(markerClass = UnstableApi.class) public static void clearCache(Context context) {
        Cache cache = getCache(context);
        if (cache != null) {
            try {
                cache.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
