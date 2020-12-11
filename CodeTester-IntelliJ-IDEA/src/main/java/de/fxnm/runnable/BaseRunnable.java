package de.fxnm.runnable;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import de.fxnm.listener.FeedbackListener;

public abstract class BaseRunnable<T> implements Runnable {

    public static Logger LOG;
    private final List<FeedbackListener> listeners = new LinkedList<>();
    private final Project project;
    private Boolean finished = false;

    public BaseRunnable(final Project project, final Class<T> loggerClass) {
        this.project = project;
        LOG = Logger.getInstance(loggerClass);
    }

    public Project project() {
        return this.project;
    }

    public void addListener(final FeedbackListener listener) {
        this.listeners.add(listener);
    }

    public void startRunnable(final String processName, final Object... details) {
        LOG.info("Started Callable, " + Arrays.deepToString(details));
        this.backgroundLoadingBar(processName);
        this.listeners.forEach(listener -> listener.scanStarting(details));
    }

    public void finishedRunnable(final Object... details) {
        if (this.finished.equals(Boolean.TRUE)) {
            LOG.info("Finished or failed Callable got already executed");
            return;
        }

        LOG.info("Finished Callable successful");
        this.finished = true;
        this.listeners.forEach(listener -> listener.scanCompleted(details));
    }

    public void failedRunnable(final Object... details) {
        if (this.finished.equals(Boolean.TRUE)) {
            LOG.info("Finished or failed Callable got already executed, " + Arrays.deepToString(details));
            return;
        }

        LOG.info("Finished Callable with error, " + Arrays.deepToString(details));
        this.finished = true;
        this.listeners.forEach(listener -> listener.scanFailed(details));
    }

    private void backgroundLoadingBar(final String processName) {
        ProgressManager.getInstance().run(new Task.Backgroundable(this.project(), processName) {
            public void run(@NotNull final ProgressIndicator progressIndicator) {
                while (BaseRunnable.this.finished.equals(Boolean.FALSE)) {
                    progressIndicator.setIndeterminate(true);
                }
            }
        });
    }
}
