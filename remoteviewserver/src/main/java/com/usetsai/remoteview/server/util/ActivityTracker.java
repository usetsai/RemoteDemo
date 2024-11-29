package com.usetsai.remoteview.server.util;

import android.app.Activity;

import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * Helper class to statically track activity creation
 * @param <T> The activity type to track
 */
public final class ActivityTracker<T extends Activity> {

    private WeakReference<T> mCurrentActivity = new WeakReference<>(null);

    @Nullable
    public <R extends T> R getCreatedActivity() {
        return (R) mCurrentActivity.get();
    }

    public void onActivityDestroyed(T activity) {
        if (mCurrentActivity.get() == activity) {
            mCurrentActivity.clear();
        }
    }

    public void handleCreate(T activity) {
        mCurrentActivity = new WeakReference<>(activity);
    }
}
