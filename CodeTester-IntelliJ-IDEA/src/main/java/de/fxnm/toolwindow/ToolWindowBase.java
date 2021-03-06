package de.fxnm.toolwindow;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.ComboBox;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;

import de.fxnm.util.CodeTesterBundle;

public class ToolWindowBase {

    private static final ComboBox<String> SIZE_COMBOBOX = new ComboBox<>(new String[]{"TEST"});
    /**
     * This combo box is only used to determine the larger.
     * This is used as a reference for the northern, eastern and western components as their height / width.
     * DO NOT USE THIS COMPONENT ANYWHERE ELSE!
     */

    private static Logger LOG;
    private final JPanel basePanel;
    private final JComponent centerComponent;
    private final JComponent centerTopComponent;
    /**
     * Currently not in use but it has to stay due to symmetry.
     */
    private final JComponent eastComponent = new JPanel();
    private final JComponent northComponent;
    private final JComponent westComponent;


    public ToolWindowBase(final Class<?> loggerClass,
                          final JComponent northComponent,
                          final JComponent westComponent,
                          final JComponent centerComponent,
                          final JComponent centerTopComponent) {
        LOG = Logger.getInstance(loggerClass);

        this.northComponent = northComponent;
        this.westComponent = westComponent;
        this.centerComponent = centerComponent;
        this.centerTopComponent = centerTopComponent;

        this.basePanel = new JPanel(new BorderLayout());

        this.setSize();
        this.createPanel();

        this.basePanel.setVisible(true);
    }

    public JPanel getPanel() {
        return this.basePanel;
    }

    private void setSize() {
        final int size = SIZE_COMBOBOX.getMinimumSize().height;
        final int height = this.westComponent.getMinimumSize().height + 2;

        this.setComponentSize(this.northComponent, 0, height);
        this.setComponentSize(this.eastComponent, size, 0);
        this.setComponentSize(this.westComponent, size, 0);

        LOG.info(CodeTesterBundle.message("plugin.toolWindow.base.setSize.successful"));
    }

    private void setComponentSize(final JComponent component, final int width, final int height) {
        final Dimension dimension = new Dimension(width, height);
        component.setMinimumSize(dimension);
        component.setPreferredSize(dimension);
    }

    private void createPanel() {
        final JPanel innerCenterPanel = new JPanel(new BorderLayout());
        innerCenterPanel.add(this.centerTopComponent, BorderLayout.NORTH);
        innerCenterPanel.add(this.centerComponent, BorderLayout.CENTER);


        final JPanel innerPanel = new JPanel(new BorderLayout());
        innerPanel.add(this.northComponent, BorderLayout.NORTH);
        innerPanel.add(innerCenterPanel, BorderLayout.CENTER);

        this.basePanel.add(this.eastComponent, BorderLayout.EAST);
        this.basePanel.add(innerPanel, BorderLayout.CENTER);
        this.basePanel.add(this.westComponent, BorderLayout.WEST);

        LOG.info(CodeTesterBundle.message("plugin.toolWindow.base.createdPanel.successful"));
    }
}
