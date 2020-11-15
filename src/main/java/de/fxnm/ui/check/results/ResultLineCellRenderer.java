package de.fxnm.ui.check.results;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class ResultLineCellRenderer extends DefaultListCellRenderer {

    private final JPanel panel = new JPanel(new BorderLayout());

    private final JLabel type = new JLabel();
    private final JLabel content = new JLabel();


    @Override
    public Component getListCellRendererComponent(final JList<?> list,
                                                  final Object value,
                                                  final int index,
                                                  final boolean isSelected,
                                                  final boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        final ResultLine resultLine = (ResultLine) value;

        this.type.setText(resultLine.getType());
        this.type.setMinimumSize(new Dimension(100, this.content.getHeight()));
        this.type.setPreferredSize(new Dimension(100, this.content.getHeight()));
        this.content.setText(resultLine.getModifiedContent());

        this.panel.add(this.type, BorderLayout.WEST);
        this.panel.add(this.content, BorderLayout.CENTER);

        this.setFont(list.getFont());

        this.setForeground(UIManager.getColor(isSelected ? "Tree.selectionForeground" : "Tree.textForeground"));
        this.setBackground(UIManager.getColor(isSelected ? "Tree.selectionBackground" : "Tree.textBackground"));


        return this.panel;
    }
}

