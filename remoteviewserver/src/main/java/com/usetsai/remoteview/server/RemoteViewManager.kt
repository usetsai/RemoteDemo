package com.usetsai.remoteview.server

import android.util.Log
import android.widget.RemoteViews
import com.usetsai.remoteview.server.util.Executors

class RemoteViewManager : IRemoteView.Stub() {

    private var mRemoteView: RemoteViews? = null

    override fun add(remoteViews: RemoteViews?) {
        Log.i(TAG, "RemoteViewManager register remoteViews:$remoteViews")
        mRemoteView = remoteViews
        Executors.MAIN_EXECUTOR.execute {
            val mainActivity = MainActivity.TRACKER.getCreatedActivity<MainActivity>()
            mainActivity?.add(remoteViews)
        }
    }

    override fun update(remoteViews: RemoteViews?) {
        Log.i(TAG, "RemoteViewManager update remoteViews:$remoteViews")
        mRemoteView = remoteViews
        Executors.MAIN_EXECUTOR.execute {
            val mainActivity = MainActivity.TRACKER.getCreatedActivity<MainActivity>()
            mainActivity?.update(remoteViews)
        }
    }

    override fun clear() {
        mRemoteView = null
        Executors.MAIN_EXECUTOR.execute {
            val mainActivity = MainActivity.TRACKER.getCreatedActivity<MainActivity>()
            mainActivity?.clear()
        }
    }

    companion object {
        const val TAG = "test_log"
    }
}