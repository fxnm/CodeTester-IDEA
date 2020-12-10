package de.fxnm.util;


import com.intellij.openapi.diagnostic.Logger;

public final class EnvironmentVariable {

    public static final String PASSWORD = "CodeTester_PASSWORD";
    public static final String USERNAME = "CodeTester_USERNAME";

    private static final Logger LOG = Logger.getInstance(EnvironmentVariable.class);

    private EnvironmentVariable() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static String get(final String name) {
        final String returnValue = System.getenv(name);
        if (returnValue == null) {
            LOG.error("Did not find a System Environment Variable for " + name);
        }
        return returnValue;
    }
}
