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
        LOG.info(String.format("%s Executed", callable.getClass()));
        return ApplicationManager.getApplication().executeOnPooledThread(callable);
    }


}
