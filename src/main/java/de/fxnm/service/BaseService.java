package de.fxnm.service;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;

import de.fxnm.runnable.BaseRunnable;

public abstract class BaseService {

    private final Project project;
    private final Set<Pair<Future<?>, BaseRunnable>> progress = new HashSet<>();


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
            this.progress.forEach(task -> {
                task.first.cancel(true);
                task.second.failedRunnable("Forced Stop");

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
