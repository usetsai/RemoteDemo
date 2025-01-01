package com.usetsai.remotesurface.lib;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceControlViewHost;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class RemoteSurfaceService extends Service {
    private static final String TAG = "RemoteSurface";
    private Messenger mMessenger;
    private View mView;
    private SurfaceControlViewHost mViewHost;

    public abstract View onCreateView(Context context);

    @Override
    public void onCreate() {
        mMessenger = new Messenger(new InternalHandler(Looper.getMainLooper()));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (mViewHost != null) {
            mViewHost.release();
            mViewHost = null;
        }
        return super.onUnbind(intent);
    }

    private class InternalHandler extends Handler {
        public InternalHandler(@NonNull Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == Messages.MSG_SURFACE_DISPLAY) {
                Log.e(TAG, "InternalHandler receive MSG_SURFACE_DISPLAY handleSurfaceDisplay.");
                handleSurfaceDisplay(msg);
            } else if (msg.what == Messages.MSG_TOUCH_EVENT) {
                Log.e(TAG, "InternalHandler receive MSG_TOUCH_EVENT handleTouchEvent.");
                handleTouchEvent(msg);
            }
        }

        private void handleSurfaceDisplay(Message msg) {
            if (mViewHost != null) {
                mViewHost.release();
            }
            if (mView == null) {
                mView = onCreateView(RemoteSurfaceService.this);
            }
            Bundle bundle = msg.getData();
            IBinder hostToken = bundle.getBinder(Messages.KEY_HOST_TOKEN);
            int displayId = bundle.getInt(Messages.KEY_HOST_TOKEN);
            int width = bundle.getInt(Messages.KEY_WIDTH);
            int height = bundle.getInt(Messages.KEY_HEIGHT);
            Display display = getSystemService(DisplayManager.class).getDisplay(displayId);
            mViewHost = new SurfaceControlViewHost(RemoteSurfaceService.this, display, hostToken);
            mViewHost.setView(mView, width, height);
            SurfaceControlViewHost.SurfacePackage pkg = mViewHost.getSurfacePackage();
            Messenger sender = msg.replyTo;
            if (sender != null) {
                try {
                    msg.replyTo.send(Messages.obtainSurfacePackage(pkg));
                } catch (RemoteException e) {
                    Log.e(TAG, "Error when reply surface package");
                    e.printStackTrace();
                }
            }
        }

        private void handleTouchEvent(Message msg) {
            if (mViewHost == null) {
                return;
            }
            Bundle bundle = msg.getData();
            MotionEvent event = bundle.getParcelable(Messages.KEY_TOUCH_EVENT);
            Log.e(TAG, "handleTouchEvent event:" + event);
            mView.dispatchTouchEvent(event);
        }
    }
}
