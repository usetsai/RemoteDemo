package com.usetsai.remotesurface.lib;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.SurfaceControlViewHost;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

public class RemoteSurfaceBinder {
    private static final String TAG = "RemoteSurface";
    private Context mContext;
    private final SurfaceView mSurfaceView;
    private ComponentName mComponent;
    private final Messenger mHandleMessenger;
    private Messenger mRemoteMessenger;
    private MessageConnection mConnection;
    private final InternalHandler mHandler;
    private SurfaceControlViewHost.SurfacePackage mSurfacePackage;

    public RemoteSurfaceBinder(Context context, SurfaceView surface, ComponentName component) {
        mContext = context;
        mSurfaceView = surface;
        mComponent = component;
        mHandler = new InternalHandler(Looper.getMainLooper());
        mHandleMessenger = new Messenger(mHandler);
        //surface.setZOrderOnTop(true);
        // setZOrderOnTop or setZOrderMediaOverlay with setOnTouchListener
        surface.setZOrderMediaOverlay(true);
        surface.setOnTouchListener(mTouchListener);
        surface.addOnAttachStateChangeListener(mAttachListener);
    }

    public void bind() {
        mHandler.post(mBindRunnable);
    }

    public void clear() {
        if (mConnection != null) {
            Log.e(TAG, "clear unbindService ComponentName:" + mComponent);
            mContext.unbindService(mConnection);
            mConnection = null;
        }
    }

    private void sendSurfaceToken() {
        mHandler.post(() -> {
            Message msg = Messages.obtainSurfaceDisplay(
                    mSurfaceView.getHostToken(),
                    mSurfaceView.getDisplay().getDisplayId(),
                    mSurfaceView.getWidth(),
                    mSurfaceView.getHeight());
            msg.replyTo = mHandleMessenger;
            try {
                mRemoteMessenger.send(msg);
            } catch (RemoteException e) {
                Log.e(TAG, "Error when send surface info");
                e.printStackTrace();
            }
        });
    }

    private class MessageConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "onServiceConnected ComponentName:" + name);
            mRemoteMessenger = new Messenger(service);
            if (mSurfaceView.isAttachedToWindow()) {
                sendSurfaceToken();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "onServiceDisconnected ComponentName:" + name);
            mRemoteMessenger = null;
            mHandler.post(() -> {
               if (mSurfacePackage != null) {
                   mSurfacePackage.release();
                   Log.e(TAG, "onServiceDisconnected SurfacePackage release ComponentName:" + name);
                   mSurfacePackage = null;
               }
            });
        }

        @Override
        public void onBindingDied(ComponentName name) {
            Log.e(TAG, "onBindingDied ComponentName:" + name);
            mHandler.removeCallbacks(mBindRunnable);
            mHandler.postDelayed(mBindRunnable, 1000);
        }
    }

    private class InternalHandler extends Handler {
        public InternalHandler(@NonNull Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == Messages.MSG_SURFACE_PACKAGE) {
                handleSurfacePackage(msg);
            }
        }

        private void handleSurfacePackage(Message msg) {
            Bundle data = msg.getData();
            mSurfacePackage = data.getParcelable(Messages.KEY_SURFACE_PACKAGE);
            mSurfaceView.setChildSurfacePackage(mSurfacePackage);
        }
    }

    private final Runnable mBindRunnable = () ->
            mContext.bindService(new Intent().setComponent(mComponent),
                    new MessageConnection(), Context.BIND_AUTO_CREATE);

    private final View.OnTouchListener mTouchListener = (v, event) -> {
        Log.e(TAG, "TouchListener event:" + event);
        if (mRemoteMessenger != null) {
            try {
                mRemoteMessenger.send(Messages.obtainTouchEvent(event));
            } catch (RemoteException e) {
                Log.e(TAG, "Error when send touch event");
                e.printStackTrace();
            }
        }
        return true;
    };

    private final View.OnAttachStateChangeListener mAttachListener = new View.OnAttachStateChangeListener() {
        @Override
        public void onViewAttachedToWindow(@NonNull View v) {
            if (mRemoteMessenger != null && mSurfacePackage == null) {
                sendSurfaceToken();
            }
        }

        @Override
        public void onViewDetachedFromWindow(@NonNull View v) {
            if (mSurfacePackage != null) {
                mSurfacePackage.release();
                mSurfacePackage = null;
            }
        }
    };
}
