package de.fxnm.toolwindow;

import com.intellij.openapi.ui.ComboBox;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class ToolWindowBase {

    private static final ComboBox<String> COMBOBOX = new ComboBox<>(new String[]{"TEST"});

    private final JPanel basePanel;

    private final JComponent northComponent;
    private final JComponent eastComponent = new JPanel();
    private final JComponent westComponent;
    private final JComponent centerComponent;
    private final JComponent centerTopComponent;


    public ToolWindowBase(final JComponent northComponent,
                          final JComponent westComponent,
                          final JComponent centerComponent,
                          final JComponent centerTopComponent) {
        this.northComponent = northComponent;
        this.westComponent = westComponent;
        this.centerComponent = centerComponent;
        this.centerTopComponent = centerTopComponent;

        this.basePanel = new JPanel(new BorderLayout());

        this.setSize();
        this.createPanel();

        this.basePanel.setVisible(true);
    }

    private void setSize() {
        final int size = COMBOBOX.getMinimumSize().height;
        final int height = this.westComponent.getMinimumSize().height + 2;

        this.setComponentSize(this.northComponent, 0, height);
        this.setComponentSize(this.eastComponent, size, 0);
        this.setComponentSize(this.westComponent, size, 0);
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
    }

    public JPanel getBasePanel() {
        return this.basePanel;
    }
}
