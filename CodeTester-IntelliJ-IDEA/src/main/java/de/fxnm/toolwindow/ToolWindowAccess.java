package de.fxnm.toolwindow;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;

import java.util.function.Consumer;
import java.util.function.Function;

import de.fxnm.toolwindow.main.toolwindow.CodeTesterToolWindowPanel;

public final class ToolWindowAccess {

    private static final Logger LOG = Logger.getInstance(ToolWindowAccess.class);

    private ToolWindowAccess() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static void actOnToolWindowPanel(final ToolWindow toolWindow,
                                            final Consumer<CodeTesterToolWindowPanel> action) {
        if (toolWindow == null) {
            LOG.warn("ToolWindow is null");
            return;
        }
        final Content content = toolWindow.getContentManager().getContent(0);
        if (content != null && content.getComponent() instanceof CodeTesterToolWindowPanel) {
            action.accept((CodeTesterToolWindowPanel) content.getComponent());
        }
    }

    public static <R> R getFromToolWindowPanel(final ToolWindow toolWindow,
                                               final Function<CodeTesterToolWindowPanel, R> action,
                                               final R defaultReturn) {
        if (toolWindow == null) {
            LOG.warn("ToolWindow is null");
            return defaultReturn;
        }

        final Content content = toolWindow.getContentManager().getContent(0);
        if (content != null && content.getComponent() instanceof CodeTesterToolWindowPanel) {
            return action.apply((CodeTesterToolWindowPanel) content.getComponent());
        }
        return defaultReturn;
    }

    public static ToolWindow toolWindow(final Project project) {
        return ToolWindowManager
                .getInstance(project)
                .getToolWindow(CodeTesterToolWindowPanel.ID_MAIN_TOOL_WINDOW);
    }
}
