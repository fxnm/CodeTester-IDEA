package de.fxnm.errorhandling;

import com.intellij.openapi.application.ApplicationInfo;

import io.sentry.Sentry;

public final class SentryErrorClient {

    private static final String DSN = "https://98650ae1abea40a6b2677b4ebc3f7d2b@o446793.ingest.sentry.io/5425844";

    private SentryErrorClient() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static void init() {
        Sentry.init(options -> {
            options.setDsn(DSN);
            options.setRelease(ApplicationInfo.getInstance().getBuild().asString());
        });
    }
}
