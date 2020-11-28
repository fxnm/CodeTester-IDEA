package de.fxnm.toolwindow.main.toolwindow;

import com.google.gson.Gson;
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
import de.fxnm.result.tree.ResultTreeNode;
import de.fxnm.result.tree.ToggleableTreeNode;
import de.fxnm.toolwindow.CodeTesterToolWindowFactory;
import de.fxnm.toolwindow.ToolWindowBase;
import de.fxnm.ui.CategoryComboBox;
import de.fxnm.ui.check.CheckResultSummaryPanel;
import de.fxnm.ui.errormessage.ErrorMessage;
import de.fxnm.ui.errormessage.ErrorMessagePanel;
import de.fxnm.ui.util.ActionToolBar;
import de.fxnm.ui.util.HorizontalBox;
import de.fxnm.util.CodeTesterBundle;
import de.fxnm.web.components.category.Category;
import de.fxnm.web.components.submission.SubmissionResult;
import icons.PluginIcons;


public class CodeTesterToolWindowPanel extends JPanel implements ConfigurationListener {

    public static final String ID_TOOL_WINDOW = "CodeTester";
    private static final String MAIN_ACTION_GROUP = "CodeTesterPluginActions";
    private static final Logger LOG = Logger.getInstance(CodeTesterToolWindowPanel.class);

    private final ToolWindow toolWindow;
    private final Project project;

    private final CategoryComboBox categoryComboBox;
    private final ErrorMessagePanel errorMessagePanel = new ErrorMessagePanel();
    private ToolWindowBase toolWindowBase;

    private CheckResultSummaryPanel checkResultSummaryPanel;


    public CodeTesterToolWindowPanel(final ToolWindow toolWindow, final Project project) {
        super(new BorderLayout());

        this.toolWindow = toolWindow;
        this.project = project;

        this.categoryComboBox = new CategoryComboBox();
        this.createToolPanel();
        this.add(this.toolWindowBase.getBasePanel());
        this.setVisible(true);
    }


    public static CodeTesterToolWindowPanel panelFor(final Project project) {
        final ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);

        final ToolWindow toolWindow = toolWindowManager.getToolWindow(ID_TOOL_WINDOW);
        if (toolWindow == null) {
            LOG.error("Couldn't get tool window for ID " + ID_TOOL_WINDOW,
                    new Gson().toJson(project));
            return null;
        }

        for (final Content currentContent : toolWindow.getContentManager().getContents()) {
            if (currentContent.getComponent() instanceof CodeTesterToolWindowPanel) {
                return (CodeTesterToolWindowPanel) currentContent.getComponent();
            }
        }

        LOG.error("Could not find tool window panel on tool window with ID " + ID_TOOL_WINDOW,
                new Gson().toJson(project));
        return null;
    }

    private void createToolPanel() {

        this.checkResultSummaryPanel = new CheckResultSummaryPanel();
        this.checkResultSummaryPanel.addMouseListener(new ToolWindowMouseListener());
        this.checkResultSummaryPanel.addKeyListener(new ToolWindowKeyboardListener());

        final HorizontalBox horizontalBox = new HorizontalBox();
        horizontalBox.addComponent(new JLabel((CodeTesterBundle.message("plugin.check.category"))));
        horizontalBox.addComponent(this.categoryComboBox.getComboBox());


        this.toolWindowBase = new ToolWindowBase(
                horizontalBox.get(),
                new ActionToolBar(ID_TOOL_WINDOW, MAIN_ACTION_GROUP, false),
                this.checkResultSummaryPanel.getPanel(),
                this.errorMessagePanel.get()
        );
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
    public void displayCheckResult(final SubmissionResult submissionResult) {
        this.checkResultSummaryPanel.setModel(submissionResult);
    }

    @Override
    public void removeCheckResult() {
        this.checkResultSummaryPanel.removeCheckResult();
    }

    @Override
    public void setCategories(final Category[] categories) {
        this.categoryComboBox.setCategories(categories);
    }

    public Category getCurrentSelectedCategory() {
        return this.categoryComboBox.getSelectedCategory();
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

        CodeTesterToolWindowFactory.createResultToolWindow(this.toolWindow, checkNode);
    }

    public void filterDisplayedResults(final boolean errors, final boolean success) {
        this.checkResultSummaryPanel.filterDisplayedResults(errors, success);
    }

    protected class ToolWindowMouseListener extends MouseAdapter {
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

    protected class ToolWindowKeyboardListener extends KeyAdapter {
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
}


