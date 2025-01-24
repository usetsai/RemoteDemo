package com.usetsai.remotesurfacehost;

import android.content.ComponentName;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class RemotePagerActivity extends FragmentActivity {
    private static final String TAG = "RemotePagerActivity";
    private ViewPager2 mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);
        initView();
    }

    private void initView() {
        mPager = findViewById(R.id.pager);
        List<RemoteItemInfo> items = new ArrayList<>();
        ComponentName simpleViewCN = new ComponentName("com.usetsai.remotesurfaceservice",
                "com.usetsai.remotesurfaceservice.SimpleViewService");
        items.add(new RemoteItemInfo("SimpleViewService", simpleViewCN));
        ComponentName simpleRecyclerViewCN = new ComponentName("com.usetsai.remotesurfaceservice",
                "com.usetsai.remotesurfaceservice.SimpleRecyclerViewService");
        items.add(new RemoteItemInfo("SimpleRecyclerViewService", simpleRecyclerViewCN));
        ComponentName simpleWebViewCN = new ComponentName("com.usetsai.remotesurfaceservice",
                "com.usetsai.remotesurfaceservice.SimpleWebViewService");
        items.add(new RemoteItemInfo("SimpleWebViewService", simpleWebViewCN));
        PagerAdapter pagerAdapter = new PagerAdapter(this);
        pagerAdapter.setItems(items);
        mPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private static class PagerAdapter extends FragmentStateAdapter {
        private final List<RemoteItemInfo> mItems = new ArrayList<>();
        public PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        public void setItems(List<RemoteItemInfo> items) {
            mItems.clear();
            mItems.addAll(items);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return RemotePagerFragment.newInstance(mItems.get(position));
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }
}