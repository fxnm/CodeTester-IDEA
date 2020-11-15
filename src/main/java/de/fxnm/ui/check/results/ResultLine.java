package de.fxnm.ui.check.results;

import lombok.Getter;

public class ResultLine {

    @Getter
    private final String type;
    @Getter
    private final String content;
    @Getter
    private final String modifiedContent;


    public ResultLine(final String type, final String content) {
        this.type = type;
        this.content = content;
        this.modifiedContent = this.content.replace(" ", "␣");
    }
}
