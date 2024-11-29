package com.usetsai.remoteview.server

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class RemoteViewService : Service() {
    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "RemoteViewService onCreate")
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.i(TAG, "RemoteViewService onBind")
        return RemoteViewManager().asBinder()
    }

    companion object {
        const val TAG = "test_log"
    }
}