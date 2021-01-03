package de.fxnm.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;

import org.jetbrains.annotations.NotNull;

import de.fxnm.toolwindow.CodeTesterToolWindowManager;
import de.fxnm.util.CodeTesterBundle;

public class RemoveResultCheckWindows extends BaseAction {

    private static final Logger LOG = Logger.getInstance(RemoveResultCheckWindows.class);

    @Override
    public void actionPerformed(@NotNull final AnActionEvent event) {
        this.project(event).ifPresent(project -> {
            try {
                CodeTesterToolWindowManager.getService(project).closeAllResultToolWindows();
            } catch (final Throwable e) {
                LOG.error(CodeTesterBundle.message("plugin.action.removeResultCheckWindow.actionFailed"), e);
            }
        });

    }

    @Override
    public void update(final @NotNull AnActionEvent event) {
        this.project(event).ifPresent(project -> {
            try {
                super.update(event);

                event.getPresentation().setEnabled(CodeTesterToolWindowManager.getService(project)
                        .existResultToolWindows());

            } catch (final Throwable e) {
                LOG.error(CodeTesterBundle.message("plugin.action.removeResultCheckWindow.updateFailed"),
                        e);
            }
        });
    }
}
