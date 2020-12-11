package de.fxnm.web.components.submission.success;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import javax.swing.Icon;

import icons.PluginIcons;
import lombok.Getter;

public class Check implements Comparable<Check> {

    private final String check;
    @Getter
    private final Result result;
    @Getter
    private final CheckOutputLineData[] output;
    @Getter
    private final String errorOutput;
    @Getter
    private final CheckFileData[] files;
    private String message;

    public Check(final String check, final Result result, final String message, final CheckOutputLineData[] output,
                 final String errorOutput, final CheckFileData[] files) {
        this.check = check;
        this.result = result;
        this.message = message;
        this.output = output;
        this.errorOutput = errorOutput;
        this.files = files;
    }

    public Icon getCheckResultIcon() {
        if (this.result == Result.SUCCESSFUL) {
            return PluginIcons.STATUS_SUCCESS;
        } else {
            return PluginIcons.STATUS_ERROR;
        }
    }

    public String getCheckName() {
        return this.check;
    }

    public void addNewErrorMessage(final String errorMessage) {
        this.message += "\n" + errorMessage;
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
