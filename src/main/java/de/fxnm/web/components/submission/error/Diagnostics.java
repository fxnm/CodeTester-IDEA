package de.fxnm.web.components.submission.error;

import de.fxnm.web.components.submission.SubmissionResult;

public class Diagnostics implements SubmissionResult {

    private final String diagnostics;

    public Diagnostics(final String diagnostics) {
        this.diagnostics = diagnostics;
    }

    @Override
    public String toString() {
        return this.diagnostics;
    }
}
