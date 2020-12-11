package de.fxnm.result.tree;

import org.jetbrains.annotations.NotNull;

import javax.swing.tree.DefaultMutableTreeNode;

import de.fxnm.web.components.submission.success.Check;
import lombok.Getter;
import lombok.Setter;

public class ResultTreeNode extends DefaultMutableTreeNode {

    @Getter
    private final Check check;
    @Setter
    @Getter
    private String toolTip;

    public ResultTreeNode(@NotNull final Check check) {
        this.check = check;
    }

    @Override
    public String toString() {
        return String.format("Check '%s' : %s", this.check.getCheckName(), this.check.getResult());
    }
}
