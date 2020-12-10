package de.fxnm.listener;

import com.intellij.openapi.project.Project;

import de.fxnm.toolwindow.main.toolwindow.CodeTesterToolWindowPanel;

public abstract class FeedbackListener extends Listener {

    private final CodeTesterToolWindowPanel toolWindow;

    public FeedbackListener(final Project project) {
        super(project);
        this.toolWindow = CodeTesterToolWindowPanel.panelFor(this.project());
    }

    public CodeTesterToolWindowPanel toolWindow() {
        return this.toolWindow;
    }
}
