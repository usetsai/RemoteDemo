package com.usetsai.remotesurfacehost;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.usetsai.remotesurface.lib.RemoteSurfaceBinder;

public class RemotePagerFragment extends Fragment {
    private static final String TAG = "RemoteActivity";
    private RemoteItemInfo mItemInfo;
    private RemoteSurfaceBinder mBinder;

    public RemotePagerFragment() { }

    public static RemotePagerFragment newInstance(RemoteItemInfo itemInfo) {
        RemotePagerFragment fragment = new RemotePagerFragment();
        fragment.setItemInfo(itemInfo);
        return fragment;
    }

    public void setItemInfo(RemoteItemInfo itemInfo) {
        mItemInfo = itemInfo;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_pager, container, false);
        SurfaceView surfaceView = rootView.findViewById(R.id.remote_surface);
        TextView textView = rootView.findViewById(R.id.remote_title);
        textView.setText(mItemInfo.getTitle());
        mBinder = new RemoteSurfaceBinder(getContext(), surfaceView, mItemInfo.getComponent());
        mBinder.bind();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mBinder != null) {
            mBinder.clear();
        }
    }
}