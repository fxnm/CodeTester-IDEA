package de.fxnm.toolwindow;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowType;
import com.intellij.ui.content.Content;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import de.fxnm.result.tree.ResultTreeNode;
import de.fxnm.toolwindow.main.toolwindow.CodeTesterToolWindowPanel;
import de.fxnm.toolwindow.result.toolwindow.ResultToolWindowPanel;
import de.fxnm.util.CodeTesterBundle;

public class CodeTesterToolWindowFactory implements ToolWindowFactory, DumbAware {

    private static final Logger LOG = Logger.getInstance(CodeTesterToolWindowFactory.class);
    private static final int MAX_LENGTH = 10;

    public static CodeTesterToolWindowFactory getService(final Project project) {
        return ServiceManager.getService(project, CodeTesterToolWindowFactory.class);
    }

    private static String restrictStringLength(final String string) {
        if (string.length() <= MAX_LENGTH) {
            return string;
        }

        final List<String> characterList = List.of(string.split(""));

        return IntStream.range(0, MAX_LENGTH)
                .mapToObj(characterList::get)
                .collect(Collectors.joining("", "", "..."));
    }

    public ResultToolWindowPanel createResultToolWindow(final @NotNull ToolWindow toolWindow,
                                                        final @NotNull ResultTreeNode resultTreeNode) {

        final ResultToolWindowPanel resultToolWindowPanel = new ResultToolWindowPanel(resultTreeNode);

        final Content content = toolWindow.getContentManager().getFactory().createContent(
                resultToolWindowPanel.getComponent(),
                restrictStringLength(resultTreeNode.getCheck().getCheckName()),
                true);
        resultToolWindowPanel.addThisContent(content);

        content.putUserData(ToolWindow.SHOW_CONTENT_ICON, Boolean.TRUE);
        content.setIcon(resultTreeNode.getCheck().getCheckResultIcon());
        content.setDescription(resultTreeNode.getCheck().getCheckName());

        toolWindow.getContentManager().addContent(content);
        toolWindow.getContentManager().setSelectedContent(content);

        LOG.info(String.format(CodeTesterBundle.message("plugin.toolWindow.factory.createNewResultToolWindow"),
                resultTreeNode.getCheck().getCheckName()));

        return resultToolWindowPanel;
    }

    @Override
    public void createToolWindowContent(@NotNull final Project project, @NotNull final ToolWindow toolWindow) {
        final Content content = toolWindow.getContentManager().getFactory().createContent(
                new CodeTesterToolWindowPanel(project),
                CodeTesterBundle.message("plugin.name"),
                false);
        content.setCloseable(false);
        toolWindow.getContentManager().addContent(content);

        toolWindow.setTitle(CodeTesterBundle.message("plugin.name"));
        toolWindow.setType(ToolWindowType.DOCKED, null);
    }
}
