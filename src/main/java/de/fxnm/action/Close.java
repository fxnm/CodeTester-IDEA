package de.fxnm.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;

import org.jetbrains.annotations.NotNull;

import static de.fxnm.toolwindow.ToolWindowAccess.toolWindow;

public class Close extends BaseAction {

    private static final Logger LOG = Logger.getInstance(Close.class);

    @Override
    public void actionPerformed(@NotNull final AnActionEvent event) {
        this.project(event).ifPresent(project -> {
            try {
                toolWindow(project).hide(null);
            } catch (final Throwable e) {
                LOG.error("Close Action failed", e);
            }
        });
    }
}
