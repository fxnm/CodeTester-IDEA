package de.fxnm.toolwindow.result.toolwindow;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.ui.content.Content;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import de.fxnm.result.tree.ResultTreeNode;
import de.fxnm.toolwindow.ToolWindowBase;
import de.fxnm.ui.check.CheckExtraDataPanel;
import de.fxnm.ui.check.CheckInOutResultPanel;
import de.fxnm.ui.errormessage.ErrorMessage;
import de.fxnm.ui.errormessage.ErrorMessagePanel;
import de.fxnm.ui.util.ActionToolBar;
import de.fxnm.ui.util.HorizontalComponentBox;
import de.fxnm.ui.util.TitleRow;
import de.fxnm.web.components.submission.success.Check;
import de.fxnm.web.components.submission.success.CheckFileData;
import icons.PluginIcons;

import static javax.swing.SwingConstants.BOTTOM;

public class ResultToolWindowPanel {

    private static final Logger LOG = Logger.getInstance(ResultToolWindowPanel.class);

    private static final String RESULT_ACTION_GROUP = "CodeTesterResultActions";
    private static String ID_RESULT_TOOL_WINDOW = "Not set yet!";
    private final ToolWindowBase baseToolWindow;
    private final JLabel checkResultIcon = new JLabel();
    private final JLabel checkResultStatus = new JLabel();
    private final ErrorMessagePanel errorMessagePanel = new ErrorMessagePanel();
    private final ResultTreeNode resultTreeNode;
    private Check check;
    private CheckInOutResultPanel checkInOutResultPanel;
    private Content content;


    public ResultToolWindowPanel(final ResultTreeNode resultTreeNode) {
        this.resultTreeNode = resultTreeNode;
        this.check = resultTreeNode.getCheck();
        ID_RESULT_TOOL_WINDOW = "CodeTesterResultWindow-" + this.check.getCheckName();

        this.addCheckErrorMessages();

        this.baseToolWindow = new ToolWindowBase(
                this.createTopLineComponent(),
                new ActionToolBar(ID_RESULT_TOOL_WINDOW, RESULT_ACTION_GROUP, false),
                this.createCenterComponent(),
                this.errorMessagePanel.get()
        );
    }

    public JPanel getComponent() {
        return this.baseToolWindow.getPanel();
    }

    public void addCheckErrorMessages() {
        for (final String error : this.check.getErrorMessage()) {
            if (!error.equals("")) {
                this.errorMessagePanel.addErrorMessage(
                        new ErrorMessage(PluginIcons.STATUS_ERROR, error, false));
            }
        }

        if (!this.check.getErrorOutput().equals("")) {
            this.errorMessagePanel.addErrorMessage(new ErrorMessage(
                    PluginIcons.STATUS_ERROR, this.check.getErrorOutput(), false));
        }
    }

    public String getCheckName() {
        return this.check.getCheckName();
    }

    public JComponent createCenterComponent() {
        final JTabbedPane tabbedPane = new JBTabbedPane(BOTTOM);

        tabbedPane.addTab("Check", this.getInAndOutputResults());

        for (final CheckFileData c : this.check.getFiles()) {
            tabbedPane.add(c.getName(), TitleRow.Companion.getTitleRow(c.getName(),
                    new CheckExtraDataPanel(c).asJComponent()));
        }
        return tabbedPane;
    }

    public void addThisContent(final Content content) {
        this.content = content;
    }

    public void newCheckIsRunning() {
        this.checkInOutResultPanel.removeLines();
        this.changeIcon(PluginIcons.UNKNOWN);
        this.errorMessagePanel.removeAllErrorMessages();
        this.checkResultStatus.setText("UNKNOWN");
    }

    public void newCheckCompleted(final Check check) {
        this.check = check;
        this.checkInOutResultPanel.addLines(check.getOutput());
        this.changeIcon(check.getCheckResultIcon());
        this.addCheckErrorMessages();
        this.checkResultStatus.setText(check.getResult().toString());
    }

    public void newCheckCompletedNotInSet() {
        this.errorMessagePanel.addErrorMessage(new ErrorMessage(PluginIcons.STATUS_ERROR,
                "This Check is not in the newest set of check results", false));
        this.checkResultStatus.setText("UNKNOWN - NOT IN TEST SET");
        this.changeIcon(PluginIcons.WARNING);
    }

    public void newCheckFailed() {
        this.errorMessagePanel.addErrorMessage(new ErrorMessage(PluginIcons.STATUS_ERROR,
                "Check Run Failed, the results of this check might not be right", false));
    }

    public Content getAsContent() {
        if (this.content == null) {
            LOG.error("Access con Content but Content is null", this.getCheckName());
        }
        return this.content;
    }

    private JComponent createTopLineComponent() {
        this.checkResultIcon.setIcon(this.resultTreeNode.getCheck().getCheckResultIcon());
        this.checkResultStatus.setText(this.check.getResult().toString());

        final HorizontalComponentBox horizontalComponentBox = new HorizontalComponentBox();

        horizontalComponentBox.addComponent(this.checkResultIcon);
        horizontalComponentBox.addComponent(this.checkResultStatus, 20);
        horizontalComponentBox.addComponent(new JLabel(this.check.getCheckName()));

        return horizontalComponentBox.get();
    }

    private JPanel getInAndOutputResults() {
        this.checkInOutResultPanel = new CheckInOutResultPanel();
        this.checkInOutResultPanel.addLines(this.check.getOutput());
        return TitleRow.Companion.getTitleRow("In and Output",
                this.checkInOutResultPanel.getComponent());
    }

    private void changeIcon(final Icon icon) {
        this.checkResultIcon.setIcon(icon);

        if (this.content == null) {
            LOG.error("No Content available for " + this.getCheckName());
            return;
        } else {
            this.content.setIcon(icon);
        }

        this.checkResultIcon.invalidate();
        this.checkResultIcon.repaint();
    }
}
