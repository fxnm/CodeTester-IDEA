package de.fxnm.ui.errormessage;

import com.intellij.openapi.diagnostic.Logger;

import java.awt.GridBagLayout;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import de.fxnm.ui.util.TitleRow;
import de.fxnm.util.ComponentStatics;
import de.fxnm.util.PooledThread;

public class ErrorMessagePanel {
    private static final Logger LOG = Logger.getInstance(ErrorMessagePanel.class);
    private static final int MS_TILL_AUTO_REMOVE = 5000;

    private final List<ErrorMessage> errorMessageList = Collections.synchronizedList(new LinkedList<>());
    private final JPanel panel;

    public ErrorMessagePanel() {
        this.panel = new JPanel(new GridBagLayout());
        this.panel.setVisible(true);
    }

    public void addErrorMessage(final ErrorMessage errorMessage) {
        this.errorMessageList.add(errorMessage);
        PooledThread.execute(new AutoRemoveListener(errorMessage));
        this.generate();
    }

    public void removeErrorMessage(final ErrorMessage errorMessage) {
        this.errorMessageList.remove(errorMessage);
        this.generate();
    }

    public void removeAllErrorMessages() {
        this.errorMessageList.clear();
        this.generate();
    }

    public JPanel get() {
        // TODO: 10.12.2020 Bug wenn keine Error Nachricht am Anfang da war
        if (this.errorMessageList.isEmpty()) {
            return new JPanel();
        }

        return TitleRow.Companion.getHideableTitleRow("Error Messages", this.panel);
    }

    public boolean isEmpty() {
        return this.errorMessageList.isEmpty();
    }

    private void generate() {
        this.panel.removeAll();
        for (int i = 0; i < this.errorMessageList.size(); i++) {
            ComponentStatics.addComponent(this.errorMessageList.get(i), 0, i, this.panel);
        }

        this.panel.invalidate();
        this.panel.repaint();
    }

    private class AutoRemoveListener implements Runnable {

        private final ErrorMessage errorMessage;

        public AutoRemoveListener(final ErrorMessage errorMessage) {
            this.errorMessage = errorMessage;
        }

        @Override
        public void run() {
            if (!this.errorMessage.getAutoRemove()) {
                return;
            }
            try {
                Thread.sleep(MS_TILL_AUTO_REMOVE);
            } catch (final InterruptedException exception) {
                LOG.error("Error Message auto remove failed", exception, this.errorMessage.toString());
            }
            ErrorMessagePanel.this.removeErrorMessage(this.errorMessage);
        }
    }
}
