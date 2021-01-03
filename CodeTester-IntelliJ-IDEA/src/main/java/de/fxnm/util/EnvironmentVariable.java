package de.fxnm.util;


import com.intellij.openapi.diagnostic.Logger;

import org.jetbrains.annotations.TestOnly;

public final class EnvironmentVariable {

    @TestOnly
    public static final String PASSWORD = "CodeTester_PASSWORD";
    @TestOnly
    public static final String USERNAME = "CodeTester_USERNAME";

    private static final Logger LOG = Logger.getInstance(EnvironmentVariable.class);

    private EnvironmentVariable() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    @TestOnly
    public static String get(final String name) {
        final String returnValue = System.getenv(name);
        if (returnValue == null) {
            LOG.error(String.format(CodeTesterBundle.message("plugin.util.environmentVariable.get.fail"),
                    name));
        }
        return returnValue;
    }
}
