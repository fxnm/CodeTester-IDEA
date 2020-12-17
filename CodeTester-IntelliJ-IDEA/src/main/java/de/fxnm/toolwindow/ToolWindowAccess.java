package de.fxnm.toolwindow;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.fxnm.exceptions.ToolWindowException;
import de.fxnm.toolwindow.main.toolwindow.CodeTesterToolWindowPanel;
import de.fxnm.util.CodeTesterBundle;

public final class ToolWindowAccess {

    private static final Logger LOG = Logger.getInstance(ToolWindowAccess.class);

    private ToolWindowAccess() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static void actOnToolWindowPanel(final ToolWindow toolWindow,
                                            final Consumer<CodeTesterToolWindowPanel> action) {
        if (toolWindow == null) {
            LOG.error(CodeTesterBundle.message("plugin.toolWindow.access.toolWindowNull"));
            return;
        }

        try {
            final Content content = getCodeTesterToolWindow(toolWindow);
            action.accept((CodeTesterToolWindowPanel) content.getComponent());
        } catch (final ToolWindowException e) {
            LOG.error(e);
        }
    }

    public static <R> R getFromToolWindowPanel(final ToolWindow toolWindow,
                                               final Function<CodeTesterToolWindowPanel, R> action,
                                               final R defaultReturn) {
        if (toolWindow == null) {
            LOG.error(CodeTesterBundle.message("plugin.toolWindow.access.toolWindowNull"));
            return defaultReturn;
        }

        try {
            final Content content = getCodeTesterToolWindow(toolWindow);
            return action.apply((CodeTesterToolWindowPanel) content.getComponent());
        } catch (final ToolWindowException e) {
            LOG.error(e);
        }

        return defaultReturn;
    }

    public static ToolWindow toolWindow(final Project project) {
        return ToolWindowManager
                .getInstance(project)
                .getToolWindow(CodeTesterToolWindowPanel.ID_MAIN_TOOL_WINDOW);
    }

    public static Content getCodeTesterToolWindow(@Nullable final ToolWindow toolWindow) throws ToolWindowException {
        if (toolWindow == null) {
            throw new ToolWindowException(CodeTesterBundle.message("plugin.toolWindow.access.toolWindowNull"));
        }


        final Content[] contents = toolWindow.getContentManager().getContents();
        for (final Content content : contents) {
            if (content.getComponent() instanceof CodeTesterToolWindowPanel) {
                return content;
            }
        }

        throw new ToolWindowException(String.format(
                CodeTesterBundle.message("plugin.toolWindow.access.getCodeTesterToolWindow.NotAvailable"),
                Arrays.stream(contents).map(Object::toString)
                        .collect(Collectors.joining(" | "))));
    }
}
