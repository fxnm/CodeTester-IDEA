package de.fxnm.service;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

import de.fxnm.runnable.BaseRunnable;
import de.fxnm.util.CodeTesterBundle;

public abstract class BaseService {

    private final List<Pair<Future<?>, BaseRunnable>> progress = new LinkedList<>();
    private final Project project;


    public BaseService(final Project project) {
        this.project = project;
    }

    public Project project() {
        return this.project;
    }


    public <T> Future<T> checkStart(final Future<T> checkFuture, final BaseRunnable runnable) {
        synchronized (this.progress) {
            if (!checkFuture.isDone()) {
                this.progress.add(new Pair<>(checkFuture, runnable));
            }
        }
        return checkFuture;
    }

    public boolean isCheckInProgress() {
        synchronized (this.progress) {
            return !this.progress.isEmpty();
        }
    }

    public void stopChecks() {
        synchronized (this.progress) {
            if (this.progress.isEmpty()) {
                return;
            }

            this.progress.forEach(task -> {
                task.first.cancel(false);
                task.second.failedRunnable(CodeTesterBundle.message("plugin.service.baseService.stopChecks.loggerMessage"),
                        CodeTesterBundle.message("plugin.service.baseService.stopChecks.toolWindowMessage"));

            });
            this.progress.clear();
        }
    }


    public <T> void checkComplete(final Future<T> task) {
        if (task == null) {
            return;
        }

        synchronized (this.progress) {
            this.progress.removeIf(t -> t.first.equals(task));
        }
    }
}
