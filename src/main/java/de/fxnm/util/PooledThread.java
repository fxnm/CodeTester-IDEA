package de.fxnm.util;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public final class PooledThread {

    private static final Logger LOG = Logger.getInstance(PooledThread.class);

    private PooledThread() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static <T> Future<T> execute(final Callable<T> callable) {
        LOG.info(callable.getClass() + " Executed");
        return ApplicationManager.getApplication().executeOnPooledThread(callable);
    }

    public static void execute(final Runnable runnable) {
        LOG.info(runnable.getClass() + " Executed");
        ApplicationManager.getApplication().executeOnPooledThread(runnable);
    }
}
