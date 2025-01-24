package com.usetsai.remotesurfacehost;

import android.content.ComponentName;

public class RemoteItemInfo {
    private ComponentName component;
    private String title;

    public RemoteItemInfo(String title, ComponentName component) {
        this.title = title;
        this.component = component;
    }

    public ComponentName getComponent() {
        return component;
    }

    public void setComponent(ComponentName component) {
        this.component = component;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}