package de.fxnm.ui.errormessage;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.components.JBList;
import com.intellij.util.ui.JBUI;

import java.awt.BorderLayout;
import java.util.concurrent.Callable;

import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JPanel;

import de.fxnm.util.PooledThread;


public class ErrorMessagePanel extends JPanel {

    public static final int MS_TILL_AUTO_REMOVE = 5000;
    private static final Logger LOG = Logger.getInstance(ErrorMessage.class);

    private JBList<ErrorMessage> errorMessageJList;
    private DefaultListModel<ErrorMessage> defaultListModel;

    public ErrorMessagePanel() {
        super(new BorderLayout());
        this.createComponent();

        this.setBorder(JBUI.Borders.empty(1));
        this.setVisible(false);
    }

    public void setErrorMessages(final Icon icon, final Boolean autoRemove, final String message) {
        final ErrorMessage errorMessage = new ErrorMessage(icon, message);
        final AutoRemoveListener autoRemoveListener = new AutoRemoveListener(errorMessage, autoRemove);
        this.defaultListModel.addElement(errorMessage);
        PooledThread.execute(autoRemoveListener);

        this.setVisible(true);
        this.invalidate();
        this.repaint();
    }

    public void removeErrorMessage(final ErrorMessage errorMessage) {
        this.defaultListModel.removeElement(errorMessage);
        if (this.errorMessageJList.isEmpty()) {
            this.setVisible(false);
        }


    }

    private void createComponent() {
        this.defaultListModel = new DefaultListModel<>();
        this.errorMessageJList = new JBList<>(this.defaultListModel);
        this.errorMessageJList.setCellRenderer(new ErrorMessageCellRenderer());
        this.errorMessageJList.setSelectionModel(new NoSelectionModel());

        this.add(this.errorMessageJList, BorderLayout.CENTER);
    }

    private final class AutoRemoveListener implements Callable<Boolean> {

        private final ErrorMessage errorMessage;
        private final Boolean autoRemove;

        private AutoRemoveListener(final ErrorMessage errorMessage, final Boolean autoRemove) {
            this.errorMessage = errorMessage;
            this.autoRemove = autoRemove;
        }


        @Override
        public Boolean call() {
            if (!this.autoRemove) {
                return true;
            }
            try {
                Thread.sleep(MS_TILL_AUTO_REMOVE);
            } catch (final InterruptedException exception) {
                LOG.error(exception);
            }

            ErrorMessagePanel.this.removeErrorMessage(this.errorMessage);
            return true;
        }
    }
}

