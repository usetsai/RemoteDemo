package com.usetsai.remotesurfacehost;

import android.content.ComponentName;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.usetsai.remotesurface.lib.RemoteSurfaceBinder;

import java.util.function.Supplier;

public class RemoteActivity extends AppCompatActivity {
    private static final String TAG = "RemoteActivity";
    private FrameLayout mChildFrame1;
    private FrameLayout mChildFrame2;
    private FrameLayout mChildFrame3;
    private RemoteSurfaceBinder mSimpleViewServiceBinder;
    private RemoteSurfaceBinder mSimpleRecyclerViewServiceBinder;
    private RemoteSurfaceBinder mSimpleWebViewServiceBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initView());
        bindView();
    }

    private View initView() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        Supplier<FrameLayout> childSupplier = () -> {
            FrameLayout child = new FrameLayout(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0);
            lp.weight = 1;
            child.setLayoutParams(lp);
            return child;
        };
        Supplier<View> lineSupplier = () -> {
            View line = new View(this);
            line.setBackgroundColor(Color.BLACK);
            LinearLayout.LayoutParams lineLp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 10);
            line.setLayoutParams(lineLp);
            return line;
        };

        mChildFrame1 = childSupplier.get();
        layout.addView(mChildFrame1);

        layout.addView(lineSupplier.get());

        mChildFrame2 = childSupplier.get();
        layout.addView(mChildFrame2);

        layout.addView(lineSupplier.get());

        mChildFrame3 = childSupplier.get();
        layout.addView(mChildFrame3);
        return layout;
    }

    private void bindView() {
        mSimpleViewServiceBinder = bindViewService(mChildFrame1,
                new ComponentName("com.usetsai.remotesurfaceservice",
                "com.usetsai.remotesurfaceservice.SimpleViewService"));
        mSimpleRecyclerViewServiceBinder = bindViewService(mChildFrame2,
                new ComponentName("com.usetsai.remotesurfaceservice",
                "com.usetsai.remotesurfaceservice.SimpleRecyclerViewService"));
        mSimpleWebViewServiceBinder = bindViewService(mChildFrame3,
                new ComponentName("com.usetsai.remotesurfaceservice",
                "com.usetsai.remotesurfaceservice.SimpleWebViewService"));
    }

    private RemoteSurfaceBinder bindViewService(FrameLayout layout, ComponentName component) {
        SurfaceView surface = new SurfaceView(this);
        FrameLayout.LayoutParams surfaceLp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        layout.addView(surface, surfaceLp);

        RemoteSurfaceBinder binder = new RemoteSurfaceBinder(this, surface, component);
        binder.bind();
        return binder;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        if (mSimpleViewServiceBinder != null) {
            Log.i(TAG, "onDestroy SimpleViewServiceBinder clear.");
            mSimpleViewServiceBinder.clear();
        }
        if (mSimpleRecyclerViewServiceBinder != null) {
            mSimpleRecyclerViewServiceBinder.clear();
        }
        if (mSimpleWebViewServiceBinder != null) {
            mSimpleWebViewServiceBinder.clear();
        }
    }
}