package de.fxnm.toolwindow;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.ui.content.Content;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import de.fxnm.result.tree.ResultTreeNode;
import de.fxnm.toolwindow.main.toolwindow.CodeTesterToolWindowPanel;
import de.fxnm.toolwindow.result.toolwindow.ResultToolWindowPanel;
import de.fxnm.web.components.submission.SubmissionResult;
import de.fxnm.web.components.submission.success.Check;
import de.fxnm.web.components.submission.success.Successful;

public class CodeTesterToolWindowManager {

    private static final Logger LOG = Logger.getInstance(CodeTesterToolWindowManager.class);

    private final Project project;
    private final List<ResultToolWindowPanel> resultWindowList = new LinkedList<>();

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


        this.resultWindowList.add(
                CodeTesterToolWindowFactory.getService(this.project).createResultToolWindow(
                        ToolWindowAccess.toolWindow(this.project),
                        resultTreeNode));
    }

    public void showCheckSummaryToolWindow() {
        final Content[] toolWindows = ToolWindowAccess.toolWindow(this.project).getContentManager().getContents();
        if (toolWindows.length == 0) {
            LOG.error("ContentManger Content Array is Empty");
            return;
        }


        Content homeScreen = null;
        for (final Content toolWindow : toolWindows) {
            if (toolWindow.getComponent() instanceof CodeTesterToolWindowPanel) {
                homeScreen = toolWindow;
                break;
            }
        }

        if (homeScreen == null) {
            LOG.error("ContentManager does not contain CheckSummaryToolWindow");
            return;
        }

        ToolWindowAccess.toolWindow(this.project).getContentManager().setSelectedContent(homeScreen);
    }

    public boolean existResultToolWindows() {
        return !this.resultWindowList.isEmpty();
    }

    public void closeAllResultToolWindows() {

        for (final ResultToolWindowPanel resultToolWindowPanel : this.resultWindowList) {
            if (this.isOpenToolWindow(resultToolWindowPanel)) {
                ToolWindowAccess.toolWindow(this.project).getContentManager().removeContent(resultToolWindowPanel.getAsContent(), true);
            }
        }

        this.resultWindowList.clear();
    }

    public void newCheckRunning() {
        for (final ResultToolWindowPanel resultToolWindowPanel : this.resultWindowList) {
            resultToolWindowPanel.newCheckIsRunning();
        }
    }

    public void newCheckCompleted(final SubmissionResult submissionResult) {
        if (!(submissionResult instanceof Successful)) {
            for (final ResultToolWindowPanel resultToolWindowPanel : this.resultWindowList) {
                resultToolWindowPanel.newCheckFailed();
            }
            return;
        }


        final List<Content> openToolWindows = Arrays.asList(ToolWindowAccess.toolWindow(this.project).getContentManager().getContents());
        final List<Check> successful = new LinkedList<>(Arrays.asList(((Successful) submissionResult).getChecks()));

        for (final ResultToolWindowPanel resultToolWindowPanel : this.resultWindowList) {
            if (!openToolWindows.contains(resultToolWindowPanel.getAsContent())) {
                resultToolWindowPanel.newCheckCompletedNotInSet();
                this.resultWindowList.remove(resultToolWindowPanel);
                continue;
            }

            final String checkName = resultToolWindowPanel.getCheckName();
            final List<Check> checkList = successful.stream().filter(c -> c.getCheckName().equals(checkName)).collect(Collectors.toList());

            if (checkList.isEmpty()) {
                resultToolWindowPanel.newCheckCompletedNotInSet();
                return;
            }

            if (checkList.size() != 1) {
                for (final Check c : checkList) {
                    c.addNewErrorMessage("In the check results were several tests with the same name.\n"
                            + " Therefore, this test may differ from the actual value.");
                }
                LOG.error("The ResultCheckSet does contain multiple checks with the same id for a CheckResultPanel", Arrays.deepToString(new List[]{checkList}));
            }

            resultToolWindowPanel.newCheckCompleted(checkList.get(0));
        }


    }

    private boolean containsCheckResult(final ResultTreeNode resultTreeNode) {
        final ResultToolWindowPanel resultToolWindowPanel = this.getPair(resultTreeNode);
        if (resultToolWindowPanel == null) {
            return false;
        }


        if (!this.isOpenToolWindow(resultToolWindowPanel)) {
            this.resultWindowList.remove(resultToolWindowPanel);
            return false;
        }

        return true;
    }

    private boolean isOpenToolWindow(final ResultToolWindowPanel resultToolWindowPanel) {
        return ToolWindowAccess.toolWindow(this.project).getContentManager()
                .getContent(resultToolWindowPanel.getAsContent().getComponent()) != null;
    }

    private ResultToolWindowPanel getPair(final ResultTreeNode resultTreeNode) {
        final String checkName = resultTreeNode.getCheck().getCheckName();
        for (final ResultToolWindowPanel pair : this.resultWindowList) {
            if (pair.getCheckName().equals(checkName)) {
                return pair;
            }
        }
        return null;
    }

    private void setFocusOn(final ResultTreeNode resultTreeNode) {
        final ResultToolWindowPanel resultToolWindowPanel = this.getPair(resultTreeNode);
        if (resultToolWindowPanel == null) {
            LOG.error("No Check Result Tool Window found under:" + resultTreeNode.getCheck().getCheckName(),
                    Arrays.deepToString(this.resultWindowList.toArray()));
            return;
        }

        ToolWindowAccess.toolWindow(this.project).getContentManager().setSelectedContent(resultToolWindowPanel.getAsContent());
    }
}
