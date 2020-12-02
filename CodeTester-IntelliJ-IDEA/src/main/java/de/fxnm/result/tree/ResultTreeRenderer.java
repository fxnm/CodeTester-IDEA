package de.fxnm.result.tree;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class ResultTreeRenderer extends DefaultTreeCellRenderer {

    public ResultTreeRenderer() {
        super();
    }

    @Override
    public Component getTreeCellRendererComponent(final JTree tree,
                                                  final Object value,
                                                  final boolean selected,
                                                  final boolean expanded,
                                                  final boolean leaf,
                                                  final int row,
                                                  final boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

        final DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

        if (node != null) {
            final Object userObject = node.getUserObject();
            if (userObject instanceof ResultTreeNode) {
                final ResultTreeNode treeNode = (ResultTreeNode) userObject;

                this.setIcon(treeNode.getIcon());
                this.setToolTipText(treeNode.getToolTip());
                this.setText(treeNode.toString());

                this.validate();
            } else {
                this.setIcon(null);
            }
        }

        this.setFont(tree.getFont());

        this.setForeground(UIManager.getColor(selected ? "Tree.selectionForeground" : "Tree.textForeground"));
        this.setBackground(UIManager.getColor(selected ? "Tree.selectionBackground" : "Tree.textBackground"));


        return this;
    }
}


