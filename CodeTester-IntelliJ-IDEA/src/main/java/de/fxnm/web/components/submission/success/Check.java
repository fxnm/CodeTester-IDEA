package de.fxnm.web.components.submission.success;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import lombok.Getter;

public class Check implements Comparable<Check> {

    private final String check;
    @Getter
    private final Result result;
    private final String message;
    @Getter
    private final CheckOutputLineData[] output;
    @Getter
    private final String errorOutput;
    @Getter
    private final CheckFileData[] files;

    public Check(final String check, final Result result, final String message, final CheckOutputLineData[] output,
                 final String errorOutput, final CheckFileData[] files) {
        this.check = check;
        this.result = result;
        this.message = message;
        this.output = output;
        this.errorOutput = errorOutput;
        this.files = files;
    }


    public String getCheckName() {
        return this.check;
    }

    public String[] getErrorMessage() {
        return this.message.split("\n");
    }

    public boolean isErrorMessageEmpty() {
        return this.message.equals("");
    }

    @Override
    public String toString() {
        return "Check{" +
                "check='" + this.check + '\'' +
                ", result='" + this.result + '\'' +
                ", message='" + this.message + '\'' +
                ", output=" + Arrays.toString(this.output) +
                ", errorOutput='" + this.errorOutput + '\'' +
                ", files=" + Arrays.toString(this.files) +
                '}';
    }

    @Override
    public int compareTo(@NotNull final Check o) {
        if (this.result == o.result) {
            return 0;
        }
        if (o.result == Result.SUCCESSFUL) {
            return 1;
        }
        return -1;
    }
}
