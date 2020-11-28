package de.fxnm.ui.check;

import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.JBEmptyBorder;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import de.fxnm.result.tree.ResultTreeModel;
import de.fxnm.result.tree.ResultTreeRenderer;
import de.fxnm.web.components.submission.SubmissionResult;
import lombok.Getter;

/**
 * Class used to create the visual representation of the overview of the results of the checks.
 */
public class CheckResultSummaryPanel {

    private final ResultTreeModel treeModel;
    @Getter
    private final Tree resultTree;

    public CheckResultSummaryPanel() {
        this.treeModel = new ResultTreeModel();
        this.resultTree = new Tree(this.treeModel);
        this.resultTree.setRootVisible(false);
        this.resultTree.setVisible(false);
        this.resultTree.setCellRenderer(new ResultTreeRenderer());
    }

    public void addMouseListener(final MouseListener mouseListener) {
        this.resultTree.addMouseListener(mouseListener);
    }

    public void addKeyListener(final KeyListener keyListener) {
        this.resultTree.addKeyListener(keyListener);
    }

    public void expandTree() {
        this.expandNode(this.resultTree, this.treeModel.getVisibleRootNode(),
                new TreePath(this.treeModel.getPathToRoot(this.treeModel.getVisibleRootNode())), 3);
    }

    public void expandNode(final JTree tree, final TreeNode node, final TreePath path, final int level) {
        if (level <= 0) {
            return;
        }

        tree.expandPath(path);

        for (int i = 0; i < node.getChildCount(); ++i) {
            final TreeNode childNode = node.getChildAt(i);
            this.expandNode(tree, childNode, path.pathByAddingChild(childNode), level - 1);
        }
    }

    public void setModel(final SubmissionResult submissionResult) {
        this.treeModel.setModel(submissionResult);
        this.resultTree.setVisible(true);
        this.resultTree.invalidate();
        this.resultTree.repaint();
        this.expandTree();
    }

    public void removeCheckResult() {
        this.resultTree.setVisible(false);
        this.treeModel.clear();
    }

    public void filterDisplayedResults(final boolean errors, final boolean success) {
        this.treeModel.filter(errors, success);
        this.resultTree.invalidate();
        this.resultTree.repaint();
        this.expandTree();
    }

    public JComponent getPanel() {
        final JBScrollPane scrollPane = new JBScrollPane();
        scrollPane.setViewportView(this.resultTree);
        scrollPane.setBorder(new JBEmptyBorder(0));
        return scrollPane;
    }
}
