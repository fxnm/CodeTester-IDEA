package de.fxnm.toolwindow;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;

import de.fxnm.ui.errormessage.ErrorMessagePanel;

public class ToolWindowBase extends JPanel {

    private final String toolWindowName;
    private final ErrorMessagePanel errorMessagePanel = new ErrorMessagePanel();
    private JBScrollPane scrollPane;
    private JComponent centerJComponent;

    public ToolWindowBase(final String toolWindowName) {
        super(new BorderLayout());

        this.toolWindowName = toolWindowName;

        this.setVisible(true);
    }

    public void createUI(final String sideActionToolbarName,
                         final JComponent headJComponent,
                         final JComponent centerJComponent,
                         final JComponent bottomJComponent) {

        this.add(this.createHorizontalToolBar(sideActionToolbarName), BorderLayout.WEST);

        this.add(this.createCenterJComponent(
                headJComponent,
                centerJComponent,
                bottomJComponent
        ), BorderLayout.CENTER);

        this.setBorder(JBUI.Borders.empty(1));
        this.setVisible(true);

        this.invalidate();
        this.repaint();
    }

    private JComponent createHorizontalToolBar(final String sideActionToolbarName) {
        final ActionGroup actionGroup = (ActionGroup) ActionManager.getInstance().getAction(sideActionToolbarName);
        final ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar(this.toolWindowName, actionGroup, false);
        final Box toolBox = Box.createHorizontalBox();
        toolBox.add(actionToolbar.getComponent());
        return toolBox;
    }

    private JComponent createCenterJComponent(final JComponent headJComponent,
                                              final JComponent centerJComponent,
                                              final JComponent bottomJComponent) {
        final JPanel toolPanel = new JPanel(new BorderLayout());
        final JPanel topToolPanel = new JPanel(new BorderLayout());

        topToolPanel.add(headJComponent, BorderLayout.NORTH);
        topToolPanel.add(this.errorMessagePanel, BorderLayout.SOUTH);

        toolPanel.add(topToolPanel, BorderLayout.NORTH);
        this.scrollPane = new JBScrollPane();
        this.centerJComponent = centerJComponent;
        this.scrollPane.setViewportView(centerJComponent);

        toolPanel.add(this.scrollPane, BorderLayout.CENTER);
        toolPanel.add(bottomJComponent, BorderLayout.SOUTH);

        return toolPanel;
    }

    public void setMessage(final Icon icon, final Boolean autoRemove, final String message) {
        this.errorMessagePanel.setErrorMessages(icon, autoRemove, message);
        this.invalidate();
        this.repaint();
        this.setVisible(true);
    }

    public void changeViewport(final Component component) {
        this.scrollPane.setViewportView(component);
        this.repaint();
    }

    public void restoreViewport() {
        this.scrollPane.setViewportView(this.centerJComponent);
        this.repaint();
    }
}
