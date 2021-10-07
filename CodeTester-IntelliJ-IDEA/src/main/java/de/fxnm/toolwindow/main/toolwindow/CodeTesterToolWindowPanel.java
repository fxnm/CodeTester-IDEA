package de.fxnm.toolwindow.main.toolwindow;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.tree.TreePath;

import de.fxnm.config.ConfigurationListener;
import de.fxnm.exceptions.ToolWindowException;
import de.fxnm.result.tree.ResultTreeNode;
import de.fxnm.result.tree.ToggleableTreeNode;
import de.fxnm.toolwindow.CodeTesterToolWindowManager;
import de.fxnm.toolwindow.ToolWindowAccess;
import de.fxnm.toolwindow.ToolWindowBase;
import de.fxnm.ui.CategoryComboBox;
import de.fxnm.ui.check.CheckResultSummaryPanel;
import de.fxnm.ui.errormessage.ErrorMessage;
import de.fxnm.ui.errormessage.ErrorMessagePanel;
import de.fxnm.ui.util.ActionToolBar;
import de.fxnm.ui.util.HorizontalComponentBox;
import de.fxnm.util.CodeTesterBundle;
import de.fxnm.util.PopupNotifier;
import de.fxnm.web.components.category.Category;
import de.fxnm.web.components.submission.SubmissionResult;
import de.fxnm.web.components.submission.success.Successful;
import icons.PluginIcons;


public class CodeTesterToolWindowPanel extends JPanel implements ConfigurationListener {

    public static final String ID_MAIN_TOOL_WINDOW = "CodeTester";
    private static final Logger LOG = Logger.getInstance(CodeTesterToolWindowPanel.class);
    private static final String MAIN_ACTION_GROUP = "CodeTesterPluginActions";
    private final CategoryComboBox categoryComboBox;
    private final ErrorMessagePanel errorMessagePanel = new ErrorMessagePanel();
    private final Project project;
    private CheckResultSummaryPanel checkResultSummaryPanel;
    private ToolWindowBase toolWindowBase;


    public CodeTesterToolWindowPanel(final Project project) {
        super(new BorderLayout());

        this.project = project;

        this.categoryComboBox = new CategoryComboBox();
        this.createToolPanel();
        this.add(this.toolWindowBase.getPanel());
        this.setVisible(true);
    }


    public static CodeTesterToolWindowPanel panelFor(final Project project) {
        final ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow(ID_MAIN_TOOL_WINDOW);

        try {
            final Content a = ToolWindowAccess.getCodeTesterToolWindow(toolWindow);
            return (CodeTesterToolWindowPanel) a.getComponent();

        } catch (final ToolWindowException e) {
            LOG.error(e);
        }

        return null;
    }

    @Override
    public void displayErrorMessage(final Boolean autoRemove, final String message) {
        this.errorMessagePanel.addErrorMessage(new ErrorMessage(PluginIcons.STATUS_ERROR, message, autoRemove));
    }

    @Override
    public void displayInfoMessage(final Boolean autoRemove, final String message) {
        this.errorMessagePanel.addErrorMessage(new ErrorMessage(PluginIcons.STATUS_INFO, message, autoRemove));
    }

    @Override
    public void displayCheckResult(final SubmissionResult submissionResult, final Project project) {
        if (submissionResult instanceof Successful) {
            this.checkResultSummaryPanel.setModel(submissionResult, project);
        } else {
            this.displayErrorMessage(true,
                    CodeTesterBundle.message("plugin.toolWindow.codeTester.displayResult.error.message"));

            PopupNotifier.notify(project,
                    CodeTesterBundle.message("plugin.toolWindow.codeTester.displayResult.error.tile"),
                    submissionResult.toString(),
                    NotificationType.ERROR, PluginIcons.STATUS_ERROR);
        }
    }

    @Override
    public void removeCheckResult() {
        this.checkResultSummaryPanel.removeCheckResult();
    }

    @Override
    public CategoryComboBox getCategories() {
        return this.categoryComboBox;
    }

    public Category getCurrentSelectedCategory() {
        return this.categoryComboBox.getSelectedCategory();
    }

    public void filterDisplayedResults(final boolean errors, final boolean success) {
        this.checkResultSummaryPanel.filterDisplayedResults(errors, success);
    }

    private void createToolPanel() {

        this.checkResultSummaryPanel = new CheckResultSummaryPanel();
        this.checkResultSummaryPanel.addMouseListener(new ToolWindowMouseListener());
        this.checkResultSummaryPanel.addKeyListener(new ToolWindowKeyboardListener());

        final HorizontalComponentBox horizontalComponentBox = new HorizontalComponentBox();
        horizontalComponentBox.addComponent(new JLabel(CodeTesterBundle.message("plugin.toolWindow.codeTester.createToolWindow.category")));
        horizontalComponentBox.addComponent(this.categoryComboBox.getComboBox());


        this.toolWindowBase = new ToolWindowBase(CodeTesterToolWindowPanel.class,
                horizontalComponentBox.get(),
                new ActionToolBar(ID_MAIN_TOOL_WINDOW, MAIN_ACTION_GROUP, false),
                this.checkResultSummaryPanel.getPanel(),
                this.errorMessagePanel.get()
        );
    }

    private void newPopUp(final TreePath treePath) {
        if (!(treePath.getLastPathComponent() instanceof ToggleableTreeNode)) {
            return;
        }

        final ToggleableTreeNode treeNode = (ToggleableTreeNode) treePath.getLastPathComponent();
        if (!(treeNode.getUserObject() instanceof ResultTreeNode)) {
            return;
        }

        final ResultTreeNode checkNode = (ResultTreeNode) treeNode.getUserObject();

        if (checkNode.getCheck() == null) {
            return;
        }

        CodeTesterToolWindowManager.getService(this.project).showResultToolWindow(checkNode);
        LOG.info(String.format(CodeTesterBundle.message("plugin.toolWindow.codeTester.newPopUp.successful"),
                checkNode.getCheck().getCheckName()));
    }

    private class ToolWindowKeyboardListener extends KeyAdapter {
        @Override
        public void keyPressed(final KeyEvent e) {
            if (e.getKeyCode() != KeyEvent.VK_ENTER) {
                return;
            }

            final TreePath treePath = CodeTesterToolWindowPanel.this.checkResultSummaryPanel.getResultTree().getSelectionPath();

            if (treePath == null) {
                return;
            }

            CodeTesterToolWindowPanel.this.newPopUp(treePath);
        }
    }

    private class ToolWindowMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(final MouseEvent e) {
            if (e.getClickCount() < 2) {
                return;
            }

            final TreePath treePath = CodeTesterToolWindowPanel.this.checkResultSummaryPanel.getResultTree().getPathForLocation(
                    e.getX(), e.getY());

            if (treePath == null) {
                return;
            }

            CodeTesterToolWindowPanel.this.newPopUp(treePath);
        }
    }
}


