// IRemoteView.aidl
package com.usetsai.remoteview.server;

// Declare any non-default types here with import statements

interface IRemoteView {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);
    void add(in RemoteViews remoteViews);

    void update(in RemoteViews remoteViews);

    void clear();
}