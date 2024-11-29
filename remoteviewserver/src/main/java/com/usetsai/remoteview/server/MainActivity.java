package com.usetsai.remoteview.server;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.usetsai.remoteview.server.util.ActivityTracker;

public class MainActivity extends AppCompatActivity {

    public static final ActivityTracker<MainActivity> TRACKER = new ActivityTracker<>();
    private FrameLayout mContainerView;
    private View mView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout layout = new FrameLayout(this);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(layout);
        mContainerView = layout;
        TRACKER.handleCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TRACKER.onActivityDestroyed(this);
    }

    public void add(RemoteViews remoteViews) {
        if (remoteViews != null) {
            mView = remoteViews.apply(this, null);
            mContainerView.removeAllViews();
            mContainerView.addView(mView);
        }
    }

    public void update(RemoteViews remoteViews) {
        if (remoteViews != null && mView != null) {
            remoteViews.reapply(MainActivity.this, mView);
        }
    }

    public void clear() {
        mView = null;
        mContainerView.removeAllViews();
    }
}