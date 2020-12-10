package de.fxnm.result.tree;

import com.intellij.openapi.diagnostic.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import de.fxnm.web.components.submission.SubmissionResult;
import de.fxnm.web.components.submission.success.Check;
import de.fxnm.web.components.submission.success.Result;
import de.fxnm.web.components.submission.success.Successful;
import lombok.Getter;

public class ResultTreeModel extends DefaultTreeModel {

    private static final Logger LOG = Logger.getInstance(ResultTreeNode.class);
    @Getter
    private final DefaultMutableTreeNode visibleRootNode;
    private Successful successfulCheckResults;

    public ResultTreeModel() {
        super(new DefaultMutableTreeNode());

        this.visibleRootNode = new DefaultMutableTreeNode();

        ((DefaultMutableTreeNode) this.getRoot()).add(this.visibleRootNode);
    }

    public void clear() {
        this.visibleRootNode.removeAllChildren();
        this.nodeStructureChanged(this.visibleRootNode);
    }

    public void setRootText(final String messageText) {
        this.visibleRootNode.setUserObject(messageText);
        this.nodeChanged(this.visibleRootNode);
    }

    public void setModel(final SubmissionResult submissionResult) {
        if (submissionResult instanceof Successful) {

            this.successfulCheckResults = (Successful) submissionResult;

            final List<Check> checks = Arrays.stream(this.successfulCheckResults.getChecks())
                    .sorted()
                    .collect(Collectors.toList());

            this.setChecks(checks);

            final int successfulCount = (int) checks.stream().filter(s -> s.getResult() == Result.SUCCESSFUL).count();
            final int failedCount = (int) checks.stream().filter(s -> s.getResult() == Result.FAILED).count();

            this.setRootText(String.format("Check result: %d | Successful: %d | Failed: %d",
                    successfulCount + failedCount, successfulCount, failedCount));
        } else {
            LOG.error(submissionResult.toString());
        }

        this.nodeStructureChanged(this.visibleRootNode);
    }

    public void filter(final boolean errors, final boolean success) {
        if (this.successfulCheckResults == null) {
            return;
        }

        this.setChecks(Arrays.stream(this.successfulCheckResults.getChecks()).filter(s -> {
            switch (s.getResult()) {
                case SUCCESSFUL:
                    return success;
                case FAILED:
                    return errors;
                default:
                    return true;
            }
        }).collect(Collectors.toList()));
    }

    private void setChecks(final List<Check> checks) {
        this.visibleRootNode.removeAllChildren();

        for (final Check check : checks) {
            final ResultTreeNode resultTreeNode = new ResultTreeNode(check);
            final ToggleableTreeNode toggleableTreeNode = new ToggleableTreeNode(resultTreeNode);
            this.visibleRootNode.add(toggleableTreeNode);
        }
        this.nodeStructureChanged(this.visibleRootNode);
    }
}
