package de.fxnm.web.components.submission.success;


import de.fxnm.web.components.submission.SubmissionResult;
import lombok.Getter;

public class Successful implements SubmissionResult {

    @Getter
    private final Check[] checks;
    private final String className;

    public Successful(final String className, final Check[] checks) {
        this.className = className;
        this.checks = checks;
    }

    @Override
    public String toString() {
        return String.format("Successful Check Results [Class: %s] [Contains %d Checks]", this.className, this.checks.length);
    }
}
