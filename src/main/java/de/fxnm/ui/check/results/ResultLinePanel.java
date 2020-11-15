package de.fxnm.ui.check.results;

import com.intellij.ui.components.JBList;
import com.intellij.util.ui.JBUI;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;

import de.fxnm.web.components.submission.success.CheckOutputLineData;

public class ResultLinePanel extends JPanel {

    private final DefaultListModel<ResultLine> resultLineDefaultListModel;
    private final JBList<ResultLine> resultLineJBList;

    public ResultLinePanel() {
        super(new BorderLayout());

        this.resultLineDefaultListModel = new DefaultListModel<>();
        this.resultLineJBList = new JBList<>(this.resultLineDefaultListModel);
        this.resultLineJBList.setCellRenderer(new ResultLineCellRenderer());

        this.add(this.resultLineJBList, BorderLayout.WEST);

        this.setBorder(JBUI.Borders.empty(1));
        this.setVisible(false);
    }

    public void addResultLines(final CheckOutputLineData... resultLines) {
        this.resultLineDefaultListModel.removeAllElements();
        for (final CheckOutputLineData data : resultLines) {
            final ResultLine resultLine = new ResultLine(data.getType(), data.getContent());
            this.resultLineDefaultListModel.addElement(resultLine);
        }

        this.setVisible(true);
        this.invalidate();
        this.repaint();
    }

    public void removeAllResultLinesLines() {
        this.resultLineDefaultListModel.removeAllElements();
        this.setVisible(true);
        this.invalidate();
        this.repaint();
    }
}
