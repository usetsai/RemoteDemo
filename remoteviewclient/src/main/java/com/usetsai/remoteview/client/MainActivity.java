package com.usetsai.remoteview.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.usetsai.remoteview.server.IRemoteView;

import java.util.function.BiFunction;

public class MainActivity extends AppCompatActivity implements ServiceConnection {

    private static final String TAG = "test_log";
    private IRemoteView mRemoteService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        layout.setOrientation(LinearLayout.VERTICAL);

        BiFunction<String, View.OnClickListener, Button> buttonFunction =
                (text, onClickListener) -> {
                    Button button = new Button(MainActivity.this);
                    button.setText(text);
                    button.setOnClickListener(onClickListener);
                    button.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    return button;
                };

        Button bindBtn = buttonFunction.apply("bind service", v -> bindService());
        layout.addView(bindBtn);

        Button addBtn = buttonFunction.apply("add", v -> add());
        layout.addView(addBtn);

        Button updateBtn = buttonFunction.apply("update", v -> update());
        layout.addView(updateBtn);

        Button replaceBtn = buttonFunction.apply("replace", v -> replace());
        layout.addView(replaceBtn);

        Button clearBtn = buttonFunction.apply("clear", v -> clear());
        layout.addView(clearBtn);

        setContentView(layout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRemoteService != null) {
            unbindService(this);
        }
    }

    private void add() {
        if (mRemoteService != null) {
            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.remote_default_layout);
            try {
                mRemoteService.add(remoteViews);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        if (mRemoteService != null) {
            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.remote_default_layout);
            remoteViews.setInt(R.id.container, "setBackgroundColor", Color.DKGRAY);
            try {
                mRemoteService.update(remoteViews);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void replace() {
        if (mRemoteService != null) {
            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.remote_replace_layout);
            try {
                mRemoteService.add(remoteViews);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void clear() {
        if (mRemoteService != null) {
            try {
                mRemoteService.clear();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void bindService() {
        Intent intent = new Intent("com.usetsai.remoteview.server")
                .setPackage("com.usetsai.remoteview.server");
        boolean result = bindService(intent, MainActivity.this, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "bindService result:" + result);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.d(TAG, "onServiceConnected componentName:" + name + ", binder:" + service);
        mRemoteService = IRemoteView.Stub.asInterface(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.d(TAG, "onServiceDisconnected componentName:" + name);
        mRemoteService = null;
    }
}