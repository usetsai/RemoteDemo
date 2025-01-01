package com.usetsai.remotesurfaceservice;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.function.Supplier;

public class RemoteActivity extends AppCompatActivity {

    private FrameLayout mChildFrame1;
    private FrameLayout mChildFrame2;
    private FrameLayout mChildFrame3;

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
        View simpleView = SimpleView.createView(this);
        mChildFrame1.addView(simpleView);

        View recyclerView = SimpleRecyclerView.createView(this);
        mChildFrame2.addView(recyclerView);

        View webView = SimpleWebView.createView(this);
        mChildFrame3.addView(webView);
    }
}