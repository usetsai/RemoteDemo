package com.usetsai.remotesurfaceservice;

import android.content.Context;
import android.view.View;

import com.usetsai.remotesurface.lib.RemoteSurfaceService;

public class SimpleRecyclerViewService extends RemoteSurfaceService {
    @Override
    public View onCreateView(Context context) {
        return SimpleRecyclerView.createView(context);
    }
}