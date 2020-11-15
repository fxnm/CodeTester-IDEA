package de.fxnm.listener;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;

import de.fxnm.toolwindow.main.toolwindow.CodeTesterToolWindowPanel;

public abstract class FeedbackListener {

    private static final Logger LOG = Logger.getInstance(FeedbackListener.class);

    private final Project project;
    private final CodeTesterToolWindowPanel toolWindow;

    public FeedbackListener(final Project project) {
        this.project = project;
        this.toolWindow = CodeTesterToolWindowPanel.panelFor(this.project());
    }

    public void scanStarting(final Object... details) {
        ApplicationManager.getApplication().invokeLater(() -> {
            this.scanStartingImp(details);
        });
    }

    public abstract void scanStartingImp(Object... details);


    public void scanCompleted(final Object... details) {
        ApplicationManager.getApplication().invokeLater(() -> {
            this.scanCompletedImp(details);
        });
    }

    public abstract void scanCompletedImp(Object... details);


    public void scanFailed(final Object... details) {
        ApplicationManager.getApplication().invokeLater(() -> {
            this.scanFailedImp(details);
        });
    }

    public abstract void scanFailedImp(Object... details);

    public Project project() {
        return this.project;
    }

    public CodeTesterToolWindowPanel toolWindow() {
        return this.toolWindow;
    }
}
