package de.fxnm.toolwindow.main.toolwindow;

import com.google.gson.Gson;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.treeStructure.Tree;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import de.fxnm.config.ConfigurationListener;
import de.fxnm.result.tree.ResultTreeModel;
import de.fxnm.result.tree.ResultTreeNode;
import de.fxnm.result.tree.ResultTreeRenderer;
import de.fxnm.result.tree.ToggleableTreeNode;
import de.fxnm.toolwindow.CodeTesterToolWindowFactory;
import de.fxnm.toolwindow.ToolWindowBase;
import de.fxnm.ui.CategoryComboBox;
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
    private final ToolWindowBase toolWindowBase;

    private JToolBar progressPanel;
    private ResultTreeModel treeModel;
    private Tree resultsTree;


    public CodeTesterToolWindowPanel(final ToolWindow toolWindow, final Project project) {
        super(new BorderLayout());

        this.toolWindow = toolWindow;
        this.project = project;

        this.categoryComboBox = new CategoryComboBox();
        this.toolWindowBase = new ToolWindowBase(ID_TOOL_WINDOW);

        this.createToolPanel();
        this.add(this.toolWindowBase);
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


    private void expandNode(final JTree tree, final TreeNode node, final TreePath path, final int level) {
        if (level <= 0) {
            return;
        }

        tree.expandPath(path);

        for (int i = 0; i < node.getChildCount(); ++i) {
            final TreeNode childNode = node.getChildAt(i);
            this.expandNode(tree, childNode, path.pathByAddingChild(childNode), level - 1);
        }
    }

    private void createToolPanel() {


        this.treeModel = new ResultTreeModel();
        this.resultsTree = new Tree(this.treeModel);
        this.resultsTree.setRootVisible(false);
        this.resultsTree.setVisible(false);

        this.resultsTree.addMouseListener(new ToolWindowMouseListener());
        this.resultsTree.addKeyListener(new ToolWindowKeyboardListener());

        this.resultsTree.setCellRenderer(new ResultTreeRenderer());


        this.progressPanel = new JToolBar(JToolBar.HORIZONTAL);
        this.progressPanel.add(Box.createHorizontalStrut(4));

        this.progressPanel.add(new JLabel((CodeTesterBundle.message("plugin.check.category"))));
        this.progressPanel.add(Box.createHorizontalStrut(4));
        this.progressPanel.add(this.categoryComboBox.getComboBox());
        this.progressPanel.add(Box.createHorizontalStrut(4));

        this.progressPanel.add(Box.createHorizontalGlue());
        this.progressPanel.setFloatable(false);
        this.progressPanel.setOpaque(false);
        this.progressPanel.setBorder(null);


        this.toolWindowBase.createUI(
                MAIN_ACTION_GROUP,
                this.progressPanel,
                this.resultsTree,
                new JPanel()
        );
    }


    @Override
    public void displayErrorMessage(final Boolean autoRemove, final String message) {
        this.toolWindowBase.setMessage(PluginIcons.STATUS_ERROR, autoRemove, message);
        this.invalidate();
        this.repaint();
    }

    @Override
    public void displayInfoMessage(final Boolean autoRemove, final String message) {
        this.toolWindowBase.setMessage(PluginIcons.STATUS_INFO, autoRemove, message);
        this.invalidate();
        this.repaint();
    }

    @Override
    public void displayCheckResult(final SubmissionResult submissionResult) {
        this.treeModel.setModel(submissionResult);
        this.resultsTree.setVisible(true);
        this.invalidate();
        this.repaint();
        this.expandTree();
    }

    @Override
    public void removeCheckResult() {
        this.resultsTree.setVisible(false);
        this.treeModel.clear();
    }

    @Override
    public void setCategories(final Category[] categories) {
        this.categoryComboBox.setCategories(categories);
    }

    private void expandTree() {
        this.expandNode(this.resultsTree, this.treeModel.getVisibleRootNode(),
                new TreePath(this.treeModel.getPathToRoot(this.treeModel.getVisibleRootNode())), 3);
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
        this.treeModel.filter(errors, success);
        this.expandTree();
    }

    protected class ToolWindowMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(final MouseEvent e) {
            if (e.getClickCount() < 2) {
                return;
            }

            final TreePath treePath = CodeTesterToolWindowPanel.this.resultsTree.getPathForLocation(
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

            final TreePath treePath = CodeTesterToolWindowPanel.this.resultsTree.getSelectionPath();

            if (treePath == null) {
                return;
            }

            CodeTesterToolWindowPanel.this.newPopUp(treePath);
        }
    }
}


