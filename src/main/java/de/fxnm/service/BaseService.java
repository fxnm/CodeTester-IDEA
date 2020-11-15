package de.fxnm.service;

import com.intellij.openapi.project.Project;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;

public abstract class BaseService {

    private final Project project;
    private final Set<Future<?>> progress = new HashSet<>();


    public BaseService(final Project project) {
        this.project = project;
    }

    public Project project() {
        return this.project;
    }

    public Set<Future<?>> progress() {
        return this.progress;
    }

    public <T> Future<T> checkStart(final Future<T> checkFuture) {
        synchronized (this.progress()) {
            if (!checkFuture.isDone()) {
                this.progress().add(checkFuture);
            }
        }
        return checkFuture;
    }

    public boolean isCheckInProgress() {
        synchronized (this.progress()) {
            return !this.progress().isEmpty();
        }
    }

    public void stopChecks() {
        synchronized (this.progress) {
            this.progress.forEach(task -> task.cancel(true));
            this.progress.clear();
        }
    }


    public <T> void checkComplete(final Future<T> task) {
        if (task == null) {
            return;
        }

        synchronized (this.progress()) {
            this.progress().remove(task);
        }
    }
}
