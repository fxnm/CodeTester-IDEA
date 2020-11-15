package de.fxnm.web.components.submission.success;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;

public class CheckFileData {
    @Getter
    private final String name;
    private final String content;

    public CheckFileData(final String name, final String content) {
        this.name = name;
        this.content = content;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String[] getContentArray() {
        final List<String> output = new LinkedList<>();
        output.add("File Name: " + this.name);
        output.add("");
        output.addAll(Arrays.stream(this.content.split("\n"))
                .map(s -> s.replace(" ", "␣"))
                .map(s -> "> " + s).collect(Collectors.toList()));

        return output.toArray(new String[0]);
    }
}
