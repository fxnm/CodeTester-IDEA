package de.fxnm.runnable;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

import de.fxnm.listener.FeedbackListener;

public abstract class BaseRunnable implements Runnable {

    private static final Logger LOG = Logger.getInstance(BaseRunnable.class);
    private final List<FeedbackListener> listeners = new LinkedList<>();
    private final Project project;
    private Boolean finished = false;

    public BaseRunnable(final Project project) {
        this.project = project;
    }

    public Project project() {
        return this.project;
    }

    public void addListener(final FeedbackListener listener) {
        this.listeners.add(listener);
    }

    public void startRunnable(final String processName, final Object... details) {
        LOG.info("Started Callable");
        this.backgroundLoadingBar(processName);
        this.listeners.forEach(listener -> listener.scanStarting(details));
    }

    public void finishedRunnable(final Object... details) {
        LOG.info("Finished Callable successful");
        this.finished = true;
        this.listeners.forEach(listener -> listener.scanCompleted(details));
    }

    public void failedRunnable(final Object... details) {
        LOG.info("Finished Callable with error");
        this.finished = true;
        this.listeners.forEach(listener -> listener.scanFailed(details));
    }

    private void backgroundLoadingBar(final String processName) {
        ProgressManager.getInstance().run(new Task.Backgroundable(this.project(), processName) {
            public void run(@NotNull final ProgressIndicator progressIndicator) {
                while (!BaseRunnable.this.finished) {
                    progressIndicator.setIndeterminate(true);
                }
            }
        });
    }
}
