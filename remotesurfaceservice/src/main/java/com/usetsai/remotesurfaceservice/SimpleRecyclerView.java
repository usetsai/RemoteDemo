package com.usetsai.remotesurfaceservice;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SimpleRecyclerView extends LinearLayout {
    private static final String TAG = "SimpleRecyclerView";

    public SimpleRecyclerView(Context context) {
        this(context, null);
    }

    public SimpleRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(Color.WHITE);

        TextView textView = new TextView(context);
        textView.setText("RecyclerView");
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(textView);

        RecyclerView recyclerView = new RecyclerView(context);
        LinearLayout.LayoutParams rvLp1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0);
        rvLp1.weight = 1;
        addView(recyclerView, rvLp1);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new SimpleAdapter(context));
        recyclerView.setOnTouchListener((v, event) -> {
            Log.i(TAG, "recyclerView touch event:" + event);
            return false;
        });
    }

    private static class SimpleAdapter extends RecyclerView.Adapter<SimpleViewHolder> {
        private final Context mContext;

        public SimpleAdapter(@NonNull Context context) {
            mContext = context;
        }

        @NonNull
        @Override
        public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView textView = new TextView(mContext);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
            return new SimpleViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
            holder.mText.setText("item " + position);
            holder.mText.setOnClickListener(v -> {
                String text = "click item pos :" + position;
                Log.i(TAG, text);
                Toast.makeText(mContext, "click item pos :" + position, Toast.LENGTH_SHORT).show();
            });
        }

        @Override
        public int getItemCount() {
            return 50;
        }
    }

    private static class SimpleViewHolder extends RecyclerView.ViewHolder {
        private final TextView mText;

        public SimpleViewHolder(TextView itemView) {
            super(itemView);
            mText = itemView;
        }
    }

    public static View createView(Context context) {
        SimpleRecyclerView layout = new SimpleRecyclerView(context);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return layout;
    }
}