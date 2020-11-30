package de.fxnm.util;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;

import java.util.concurrent.Future;

public final class PooledThread {

    private static final Logger LOG = Logger.getInstance(PooledThread.class);

    private PooledThread() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static Future<?> execute(final Runnable runnable) {
        LOG.info(runnable.getClass() + " Executed");
        return ApplicationManager.getApplication().executeOnPooledThread(runnable);
    }
}
