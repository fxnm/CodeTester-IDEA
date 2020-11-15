package de.fxnm.toolwindow.result.toolwindow;

import com.intellij.ui.components.JBList;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.Border;

import de.fxnm.result.tree.ResultTreeNode;
import de.fxnm.toolwindow.ToolWindowBase;
import de.fxnm.ui.check.results.ResultLinePanel;
import de.fxnm.web.components.submission.success.Check;
import de.fxnm.web.components.submission.success.CheckFileData;
import icons.PluginIcons;

public class ResultToolWindowPanel {

    private static final String ID_RESULT_TOOL_WINDOW = "CodeTesterResultWindow";
    private static final String RESULT_ACTION_GROUP = "CodeTesterResultActions";
    public static final int HORIZONTAL_STRUT_WIDTH = 15;
    private final ResultTreeNode resultTreeNode;
    private final Check check;

    private final int borderTopBottom = 7;
    private final int borderLeftRight = 2;
    private final Border defaultBorder = BorderFactory.createEmptyBorder(this.borderTopBottom, this.borderLeftRight, this.borderTopBottom, this.borderLeftRight);
    private final List<JCheckBox> checkBoxList = new LinkedList<>();
    private final ToolWindowBase baseToolWindow;
    private final ResultLinePanel resultLinePanel = new ResultLinePanel();


    public ResultToolWindowPanel(final ResultTreeNode resultTreeNode) {

        this.resultTreeNode = resultTreeNode;
        this.check = resultTreeNode.getCheck();


        this.baseToolWindow = new ToolWindowBase(ID_RESULT_TOOL_WINDOW);
        this.baseToolWindow.createUI(
                RESULT_ACTION_GROUP,
                this.createTopLineComponent(),
                this.addItemsToScrollPanel(),
                this.createBottomLineComponent()
        );

        for (final String error : this.check.getErrorMessage()) {
            if (!error.equals("")) {
                this.baseToolWindow.setMessage(PluginIcons.STATUS_ERROR, false, error);
            }
        }

        if (!this.check.getErrorOutput().equals("")) {
            this.baseToolWindow.setMessage(PluginIcons.STATUS_ERROR, false, this.check.getErrorOutput());
        }
    }

    public JPanel getComponent() {
        return this.baseToolWindow;
    }


    private JComponent createTopLineComponent() {
        final Box horizontalBox = Box.createHorizontalBox();
        horizontalBox.setBorder(this.defaultBorder);

        horizontalBox.add(new JLabel(this.resultTreeNode.getIcon()));
        horizontalBox.add(Box.createHorizontalStrut(4));
        horizontalBox.add(new JLabel(this.check.getResult().toString()));

        horizontalBox.add(Box.createHorizontalStrut(HORIZONTAL_STRUT_WIDTH));

        horizontalBox.add(new JLabel(this.check.getCheckName()));

        return horizontalBox;
    }

    public JComponent createBottomLineComponent() {
        final Box horizontalBox = Box.createHorizontalBox();
        horizontalBox.setBorder(this.defaultBorder);

        for (final CheckFileData file : this.check.getFiles()) {
            final JCheckBox checkBox = new JCheckBox(file.toString());
            this.checkBoxList.add(checkBox);

            checkBox.addItemListener(new CheckBoxItemListener(checkBox, file));

            horizontalBox.add(checkBox);
            horizontalBox.add(Box.createHorizontalStrut(10));
        }

        return horizontalBox;
    }


    private ResultLinePanel addItemsToScrollPanel() {
        this.resultLinePanel.addResultLines(this.check.getOutput());
        return this.resultLinePanel;
    }

    protected class CheckBoxItemListener implements ItemListener {
        private final CheckFileData checkFileData;
        private final JCheckBox checkBox;

        protected CheckBoxItemListener(final JCheckBox checkBox, final CheckFileData checkFileData) {
            this.checkBox = checkBox;
            this.checkFileData = checkFileData;
        }

        @Override
        public void itemStateChanged(final ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {

                for (final JCheckBox c : ResultToolWindowPanel.this.checkBoxList.stream()
                        .filter(s -> !s.equals(this.checkBox))
                        .collect(Collectors.toList())) {

                    c.setSelected(false);
                }

                final JList<String> list = new JBList<>("File: " + this.checkFileData.getName());
                list.setListData(this.checkFileData.getContentArray());

                ResultToolWindowPanel.this.baseToolWindow.changeViewport(list);

            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                ResultToolWindowPanel.this.baseToolWindow.restoreViewport();
            }
        }
    }
}
