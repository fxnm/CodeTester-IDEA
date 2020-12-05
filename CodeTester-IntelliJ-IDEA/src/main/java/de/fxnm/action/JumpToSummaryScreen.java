package de.fxnm.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;

import org.jetbrains.annotations.NotNull;

import de.fxnm.toolwindow.CodeTesterToolWindowManager;

public class JumpToSummaryScreen extends BaseAction {

    private static final Logger LOG = Logger.getInstance(JumpToSummaryScreen.class);

    @Override
    public void actionPerformed(@NotNull final AnActionEvent event) {
        this.project(event).ifPresent(project -> {
            try {
                CodeTesterToolWindowManager.getService(project).showCheckSummaryToolWindow();
            } catch (final Throwable e) {
                LOG.error("Jump to Summary Screen Failed", e);
            }
        });
    }

    @Override
    public void update(final @NotNull AnActionEvent event) {
    }
}
