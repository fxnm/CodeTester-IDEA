package de.fxnm.toolwindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowType;
import com.intellij.ui.content.Content;

import org.jetbrains.annotations.NotNull;

import de.fxnm.result.tree.ResultTreeNode;
import de.fxnm.toolwindow.main.toolwindow.CodeTesterToolWindowPanel;
import de.fxnm.toolwindow.result.toolwindow.ResultToolWindowPanel;
import de.fxnm.util.CodeTesterBundle;

public class CodeTesterToolWindowFactory implements ToolWindowFactory {

    public static void createResultToolWindow(final @NotNull ToolWindow toolWindow,
                                              final ResultTreeNode resultTreeNode) {
        final Content content = toolWindow.getContentManager().getFactory().createContent(
                new ResultToolWindowPanel(resultTreeNode).getComponent(),
                resultTreeNode.getCheck().getCheckName(),
                true);
        content.putUserData(ToolWindow.SHOW_CONTENT_ICON, Boolean.TRUE);

        content.setIcon(resultTreeNode.getIcon());

        toolWindow.getContentManager().addContent(content);
        toolWindow.getContentManager().setSelectedContent(content);
    }

    @Override
    public void createToolWindowContent(@NotNull final Project project, @NotNull final ToolWindow toolWindow) {
        final Content content = toolWindow.getContentManager().getFactory().createContent(
                new CodeTesterToolWindowPanel(toolWindow, project),
                CodeTesterBundle.message("plugin.name"),
                false);
        content.setCloseable(false);
        toolWindow.getContentManager().addContent(content);

        toolWindow.setTitle(CodeTesterBundle.message("plugin.name"));
        toolWindow.setType(ToolWindowType.DOCKED, null);
    }
}
