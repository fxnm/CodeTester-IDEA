package de.fxnm.listener;

import com.intellij.openapi.project.Project;

import java.util.Optional;

import de.fxnm.toolwindow.main.toolwindow.CodeTesterToolWindowPanel;

import static java.util.Optional.ofNullable;

public abstract class FeedbackListener extends Listener {

    private final CodeTesterToolWindowPanel toolWindow;

    public FeedbackListener(final Project project) {
        super(project);
        this.toolWindow = CodeTesterToolWindowPanel.panelFor(this.project());
    }


    public Optional<CodeTesterToolWindowPanel> toolWindow() {
        return ofNullable(this.toolWindow);
    }
}
