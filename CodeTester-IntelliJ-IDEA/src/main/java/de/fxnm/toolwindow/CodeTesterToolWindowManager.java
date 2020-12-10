package de.fxnm.toolwindow;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
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

    public void newCheckRunning() {
        for (final Pair<Content, ResultToolWindowPanel> resultToolWindowPanelPair : this.resultWindowPairList) {
            resultToolWindowPanelPair.second.newCheckIsRunning();
        }
    }

    public void newCheckCompleted(final SubmissionResult submissionResult) {
        if (!(submissionResult instanceof Successful)) {
            for (final Pair<Content, ResultToolWindowPanel> resultToolWindowPanelPair : this.resultWindowPairList) {
                resultToolWindowPanelPair.second.newCheckFailed();
            }
            return;
        }


        final List<Content> openToolWindows = Arrays.asList(ToolWindowAccess.toolWindow(this.project).getContentManager().getContents());
        final List<Check> successful = new LinkedList<>(Arrays.asList(((Successful) submissionResult).getChecks()));

        for (final Pair<Content, ResultToolWindowPanel> resultToolWindowPanelPair : this.resultWindowPairList) {
            if (!openToolWindows.contains(resultToolWindowPanelPair.first)) {
                resultToolWindowPanelPair.second.newCheckCompletedNotInSet();
                this.resultWindowPairList.remove(resultToolWindowPanelPair);
                continue;
            }

            final String checkName = resultToolWindowPanelPair.second.getCheckName();
            final List<Check> checkList = successful.stream().filter(c -> c.getCheckName().equals(checkName)).collect(Collectors.toList());

            if (checkList.isEmpty()) {
                resultToolWindowPanelPair.second.newCheckCompletedNotInSet();
                return;
            }

            if (checkList.size() != 1) {
                for (final Check c : checkList) {
                    c.addNewErrorMessage("In the check results were several tests with the same name.\n"
                            + " Therefore, this test may differ from the actual value.");
                }
                LOG.error("The ResultCheckSet does contain multiple checks with the same id for a CheckResultPanel", Arrays.deepToString(new List[]{checkList}));
            }

            resultToolWindowPanelPair.second.newCheckCompleted(checkList.get(0));
        }


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
}
