package de.fxnm.ui.errormessage;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.UIManager;

public class ErrorMessageCellRenderer extends DefaultListCellRenderer {

    ErrorMessageCellRenderer() {
        super();
    }


    @Override
    public Component getListCellRendererComponent(final JList<?> list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);


        final ErrorMessage errorMessage = (ErrorMessage) value;

        if (errorMessage != null) {
            this.setText(errorMessage.toString());
            this.setIcon(errorMessage.getIcon());
        }

        this.setForeground(UIManager.getColor("Tree.textForeground"));
        this.setBackground(UIManager.getColor("Tree.textBackground"));

        return this;

    }
}
