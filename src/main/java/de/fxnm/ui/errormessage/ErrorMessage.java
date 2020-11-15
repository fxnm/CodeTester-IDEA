package de.fxnm.ui.errormessage;

import javax.swing.Icon;
import javax.swing.JComponent;

public class ErrorMessage extends JComponent {

    private final String errorMessage;
    private final Icon icon;

    public ErrorMessage(final Icon icon, final String errorMessage) {
        this.icon = icon;
        this.errorMessage = errorMessage;
    }


    public Icon getIcon() {
        return this.icon;
    }

    @Override
    public String toString() {
        return this.errorMessage;
    }
}
