package de.fxnm.toolwindow;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.ui.content.Content;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import de.fxnm.result.tree.ResultTreeNode;
import de.fxnm.toolwindow.main.toolwindow.CodeTesterToolWindowPanel;
import de.fxnm.toolwindow.result.toolwindow.ResultToolWindowPanel;

public class CodeTesterToolWindowManager {

    private static final Logger LOG = Logger.getInstance(CodeTesterToolWindowManager.class);

    private final Project project;
    private final List<Pair<Content, ResultToolWindowPanel>> resultWindowPairList = new LinkedList<>();

    public CodeTesterToolWindowManager(final Project project) {
        this.project = project;
    }

    public static CodeTesterToolWindowManager getService(final Project project) {
        return ServiceManager.getService(project, CodeTesterToolWindowManager.class);
    }

    public void showResultToolWindow(final ResultTreeNode resultTreeNode) {
        if (this.containsCheckResult(resultTreeNode)) {
            this.setFocusOn(resultTreeNode);
            return;
        }

        final Pair<Content, ResultToolWindowPanel> createdToolWindow =
                CodeTesterToolWindowFactory.getService(this.project).createResultToolWindow(
                        ToolWindowAccess.toolWindow(this.project),
                        resultTreeNode);

        this.resultWindowPairList.add(createdToolWindow);
    }

    public void showCheckSummaryToolWindow() {
        final Content homeScreen = ToolWindowAccess.toolWindow(this.project).getContentManager().getContents()[0];
        if (homeScreen == null) {
            LOG.error("Did not found home Screen at ContentManger Content Positon 0");
            return;
        }

        if (!(homeScreen.getComponent() instanceof CodeTesterToolWindowPanel)) {
            LOG.error("Content at position is not the Home Screen", homeScreen.getDescription());
            return;
        }


        ToolWindowAccess.toolWindow(this.project).getContentManager().setSelectedContent(homeScreen);
    }

    private boolean containsCheckResult(final ResultTreeNode resultTreeNode) {
        final Pair<Content, ResultToolWindowPanel> pair = this.getPair(resultTreeNode);
        if (pair == null) {
            return false;
        }


        if (!this.isOpenToolWindow(pair)) {
            this.resultWindowPairList.remove(pair);
            return false;
        }

        return true;
    }

    private boolean isOpenToolWindow(final Pair<Content, ResultToolWindowPanel> pair) {
        return ToolWindowAccess.toolWindow(this.project).getContentManager()
                .getContent(pair.second.getComponent()) != null;
    }

    private Pair<Content, ResultToolWindowPanel> getPair(final ResultTreeNode resultTreeNode) {
        final String checkName = resultTreeNode.getCheck().getCheckName();
        for (final Pair<Content, ResultToolWindowPanel> pair : this.resultWindowPairList) {
            if (pair.second.getCheckName().equals(checkName)) {
                return pair;
            }
        }
        return null;
    }

    private void setFocusOn(final ResultTreeNode resultTreeNode) {
        final Pair<Content, ResultToolWindowPanel> pair = this.getPair(resultTreeNode);
        if (pair == null) {
            LOG.error("No Check Result Tool Window found under:" + resultTreeNode.getCheck().getCheckName(),
                    Arrays.deepToString(this.resultWindowPairList.toArray()));
            return;
        }

        ToolWindowAccess.toolWindow(this.project).getContentManager().setSelectedContent(pair.first);
    }

    public boolean existResultToolWindows() {
        return !this.resultWindowPairList.isEmpty();
    }

    public void closeAllResultToolWindows() {

        for (final Pair<Content, ResultToolWindowPanel> pair : this.resultWindowPairList) {
            if (this.isOpenToolWindow(pair)) {
                ToolWindowAccess.toolWindow(this.project).getContentManager().removeContent(pair.first, true);
            }
        }

        this.resultWindowPairList.clear();
    }
}
