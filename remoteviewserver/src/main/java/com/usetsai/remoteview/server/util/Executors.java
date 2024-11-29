package com.usetsai.remoteview.server.util;

import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Various different executors used in Launcher
 */
public class Executors {

    private static final int POOL_SIZE =
            Math.max(Runtime.getRuntime().availableProcessors(), 2);
    private static final int KEEP_ALIVE = 1;

    /** Dedicated executor instances for work depending on other packages. */
    private static final Map<String, LooperExecutor> PACKAGE_EXECUTORS = new ConcurrentHashMap<>();

    /**
     * An {@link ThreadPoolExecutor} to be used with async task with no limit on the queue size.
     */
    public static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            POOL_SIZE, POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    /**
     * Returns the executor for running tasks on the main thread.
     */
    public static final LooperExecutor MAIN_EXECUTOR =
            new LooperExecutor(Looper.getMainLooper());

    /**
     * A background executor for using time sensitive actions where user is waiting for response.
     */
    public static final LooperExecutor UI_HELPER_EXECUTOR =
            new LooperExecutor(
                    createAndStartNewLooper("UiThreadHelper", Process.THREAD_PRIORITY_FOREGROUND));

    /**
     * Utility method to get a started handler thread statically
     */
    public static Looper createAndStartNewLooper(String name) {
        return createAndStartNewLooper(name, Process.THREAD_PRIORITY_DEFAULT);
    }

    /**
     * Utility method to get a started handler thread statically with the provided priority
     */
    public static Looper createAndStartNewLooper(String name, int priority) {
        HandlerThread thread = new HandlerThread(name, priority);
        thread.start();
        return thread.getLooper();
    }

    /**
     * Executor used for running Launcher model related tasks (eg loading icons or updated db)
     */
    public static final LooperExecutor MODEL_EXECUTOR =
            new LooperExecutor(createAndStartNewLooper("launcher-loader"));

    /**
     * Returns and caches a single thread executor for a given package.
     *
     * @param packageName Package associated with the executor.
     */
    public static LooperExecutor getPackageExecutor(String packageName) {
        return PACKAGE_EXECUTORS.computeIfAbsent(
                packageName, p -> new LooperExecutor(
                        createAndStartNewLooper(p, Process.THREAD_PRIORITY_DEFAULT)));
    }

    /**
     * A simple ThreadFactory to set the thread name and priority when used with executors.
     */
    public static class SimpleThreadFactory implements ThreadFactory {

        private final int mPriority;
        private final String mNamePrefix;

        private final AtomicInteger mCount = new AtomicInteger(0);

        public SimpleThreadFactory(String namePrefix, int priority) {
            mNamePrefix = namePrefix;
            mPriority = priority;
        }

        @Override
        public Thread newThread(Runnable runnable) {
            Thread t = new Thread(() -> {
                Process.setThreadPriority(mPriority);
                runnable.run();
            }, mNamePrefix + mCount.incrementAndGet());
            return t;
        }
    }
}
