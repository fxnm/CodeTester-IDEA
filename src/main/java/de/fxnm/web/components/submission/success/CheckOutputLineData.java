package de.fxnm.web.components.submission.success;

import lombok.Getter;

public class CheckOutputLineData {

    @Getter
    private final String type;
    @Getter
    private final String content;

    public CheckOutputLineData(final String type, final String content) {
        this.type = type;
        this.content = content;
    }

    @Override
    public String toString() {
        return String.format("%-10s >%s", this.type, this.content.replace(" ", "␣"));
    }
}
