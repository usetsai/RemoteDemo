package com.usetsai.remoteview.server.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Extension of {@link AbstractExecutorService} which executed on a provided looper.
 */
public class LooperExecutor extends AbstractExecutorService {

    private final Handler mHandler;

    public LooperExecutor(Looper looper) {
        mHandler = new Handler(looper);
    }

    public Handler getHandler() {
        return mHandler;
    }

    @Override
    public void execute(Runnable runnable) {
        if (getHandler().getLooper() == Looper.myLooper()) {
            runnable.run();
        } else {
            getHandler().post(runnable);
        }
    }

    /**
     * Same as execute, but never runs the action inline.
     */
    public void post(Runnable runnable) {
        getHandler().post(runnable);
    }

    /**
     * Not supported and throws an exception when used.
     */
    @Override
    @Deprecated
    public void shutdown() {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported and throws an exception when used.
     */
    @Override
    @Deprecated
    public List<Runnable> shutdownNow() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    /**
     * Not supported and throws an exception when used.
     */
    @Override
    @Deprecated
    public boolean awaitTermination(long l, TimeUnit timeUnit) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the thread for this executor
     */
    public Thread getThread() {
        return getHandler().getLooper().getThread();
    }

    /**
     * Returns the looper for this executor
     */
    public Looper getLooper() {
        return getHandler().getLooper();
    }

    /**
     * Set the priority of a thread, based on Linux priorities.
     * @param priority Linux priority level, from -20 for highest scheduling priority
     *                to 19 for lowest scheduling priority.
     * @see Process#setThreadPriority(int, int)
     */
    public void setThreadPriority(int priority) {
        Process.setThreadPriority(((HandlerThread) getThread()).getThreadId(), priority);
    }
}
