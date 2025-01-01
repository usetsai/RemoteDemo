package com.usetsai.remotesurfaceservice;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SimpleView extends LinearLayout {

    private TextView mTextView;
    private ImageView mImageView;
    private boolean mTextScale = false;
    private boolean mImageVisible = true;

    public SimpleView(Context context) {
        this(context, null);
    }

    public SimpleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(Color.WHITE);
        initTextAndImageView(context);
        initBtnView(context);
    }

    private void initTextAndImageView(Context context) {
        LinearLayout childLLayout = new LinearLayout(context);
        childLLayout.setOrientation(LinearLayout.HORIZONTAL);
        childLLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(childLLayout);

        TextView textView = new TextView(context);
        textView.setText("Hello World");
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        childLLayout.addView(textView);
        mTextView = textView;

        ImageView imageView = new ImageView(context);
        imageView.setImageResource(R.mipmap.ic_launcher_round);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        childLLayout.addView(imageView);
        View.ALPHA.set(imageView, mImageVisible ? 1.0f : 0.0f);
        mImageView = imageView;

        EditText editText = new EditText(context);
        editText.setHint("EditText");
        editText.setLayoutParams(new ViewGroup.LayoutParams(400,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        childLLayout.addView(editText);
    }

    private void initBtnView(Context context) {
        LinearLayout childLLayout = new LinearLayout(context);
        childLLayout.setOrientation(LinearLayout.HORIZONTAL);
        childLLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(childLLayout);

        Button scaleButton = new Button(context);
        scaleButton.setText(mTextScale ? "resetScale" : "scaleText");
        scaleButton.setOnClickListener(v -> {
            mTextScale = !mTextScale;
            float scale = mTextScale ? 0.5f : 1.0f;
            mTextView.animate().scaleX(scale).scaleY(scale).start();
            scaleButton.setText(mTextScale ? "resetScale" : "scaleText");
        });
        scaleButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        childLLayout.addView(scaleButton);

        Button showButton = new Button(context);
        showButton.setText(mImageVisible ? "hideImg" : "showImg");
        showButton.setOnClickListener(v -> {
            mImageVisible = !mImageVisible;
            mImageView.animate().alpha(mImageVisible ? 1.0f : 0.0f).start();
            showButton.setText(mImageVisible ? "hideImg" : "showImg");
        });
        showButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        childLLayout.addView(showButton);
    }

    public static View createView(Context context) {
        SimpleView layout = new SimpleView(context);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return layout;
    }
}