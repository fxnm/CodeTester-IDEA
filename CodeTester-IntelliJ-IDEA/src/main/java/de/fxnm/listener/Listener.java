package de.fxnm.listener;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;

public abstract class Listener {

    private final Project project;

    public Listener(final Project project) {
        this.project = project;
    }

    public void scanStarting(final Object... details) {
        ApplicationManager.getApplication().invokeLater(() -> this.scanStartingImp(details));
    }

    public abstract void scanStartingImp(Object... details);


    public void scanCompleted(final Object... details) {
        ApplicationManager.getApplication().invokeLater(() -> this.scanCompletedImp(details));
    }

    public abstract void scanCompletedImp(Object... details);


    public void scanFailed(final Object... details) {
        ApplicationManager.getApplication().invokeLater(() -> this.scanFailedImp(details));
    }

    public abstract void scanFailedImp(Object... details);

    public Project project() {
        return this.project;
    }
}
