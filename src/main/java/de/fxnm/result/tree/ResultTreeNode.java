package de.fxnm.result.tree;

import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;

import de.fxnm.web.components.submission.success.Check;
import de.fxnm.web.components.submission.success.Result;
import icons.PluginIcons;
import lombok.Getter;
import lombok.Setter;

public class ResultTreeNode extends DefaultMutableTreeNode {

    @Getter
    private final Icon icon;
    @Getter
    private final Check check;
    @Setter
    @Getter
    private String toolTip;

    public ResultTreeNode(@NotNull final Check check) {
        this.check = check;
        if (check.getResult() == Result.SUCCESSFUL) {
            this.icon = PluginIcons.STATUS_SUCCESS;
        } else {
            this.icon = PluginIcons.STATUS_ERROR;
        }

    }

    @Override
    public String toString() {
        return String.format("Check '%s' : %s", this.check.getCheckName(), this.check.getResult());
    }
}
