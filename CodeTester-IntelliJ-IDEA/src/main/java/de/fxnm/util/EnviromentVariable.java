package de.fxnm.util;


import com.intellij.openapi.diagnostic.Logger;

public final class EnviromentVariable {

    public static final String PASSWORD = "CodeTester_password";
    public static final String USERNAME = "CodeTester_username";

    private static final Logger LOG = Logger.getInstance(EnviromentVariable.class);

    private EnviromentVariable() throws IllegalAccessException {
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
