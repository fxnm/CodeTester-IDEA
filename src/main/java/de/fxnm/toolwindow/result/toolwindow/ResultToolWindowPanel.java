package de.fxnm.toolwindow.result.toolwindow;

import com.intellij.ui.components.JBTabbedPane;

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
import de.fxnm.ui.util.HorizontalBox;
import de.fxnm.ui.util.TitleRow;
import de.fxnm.web.components.submission.success.Check;
import de.fxnm.web.components.submission.success.CheckFileData;
import icons.PluginIcons;

import static javax.swing.SwingConstants.BOTTOM;

public class ResultToolWindowPanel {

    private static final String ID_RESULT_TOOL_WINDOW = "CodeTesterResultWindow";
    private static final String RESULT_ACTION_GROUP = "CodeTesterResultActions";
    private final ResultTreeNode resultTreeNode;
    private final Check check;

    private final ToolWindowBase baseToolWindow;
    private final ErrorMessagePanel errorMessagePanel = new ErrorMessagePanel();
    private CheckInOutResultPanel checkInOutResultPanel;


    public ResultToolWindowPanel(final ResultTreeNode resultTreeNode) {

        this.resultTreeNode = resultTreeNode;
        this.check = resultTreeNode.getCheck();

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

        final JPanel invisible = new JPanel();
        invisible.setVisible(false);

        this.baseToolWindow = new ToolWindowBase(
                this.createTopLineComponent(),
                new ActionToolBar(ID_RESULT_TOOL_WINDOW, RESULT_ACTION_GROUP, false),
                this.createCenterComponent(),
                this.errorMessagePanel.isEmpty()
                        ? invisible
                        : TitleRow.Companion.getHideableTitleRow("Error Messages", this.errorMessagePanel.get())
        );
    }

    public JPanel getComponent() {
        return this.baseToolWindow.getBasePanel();
    }


    private JComponent createTopLineComponent() {
        final HorizontalBox horizontalBox = new HorizontalBox();

        horizontalBox.addComponent(new JLabel(this.resultTreeNode.getIcon()));
        horizontalBox.addComponent(new JLabel(this.check.getResult().toString()), 20);
        horizontalBox.addComponent(new JLabel(this.check.getCheckName()));

        return horizontalBox.get();
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


    private JPanel getInAndOutputResults() {
        this.checkInOutResultPanel = new CheckInOutResultPanel(this.check.getOutput());
        return TitleRow.Companion.getTitleRow("In and Output",
                this.checkInOutResultPanel.getAsScrollPanel());
    }
}
