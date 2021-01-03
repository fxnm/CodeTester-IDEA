package de.fxnm.errorhandling;

import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.diagnostic.Logger;

import de.fxnm.util.CodeTesterBundle;
import io.sentry.Sentry;

public final class SentryErrorClient {

    private static final String DSN = "https://98650ae1abea40a6b2677b4ebc3f7d2b@o446793.ingest.sentry.io/5425844";
    private static final Logger LOG = Logger.getInstance(SentryErrorClient.class);

    private SentryErrorClient() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static void initializeSentry() {
        Sentry.init(options -> {
            options.setDsn(DSN);
            options.setRelease(ApplicationInfo.getInstance().getBuild().asString());
        });

        LOG.info(CodeTesterBundle.message("plugin.error.sentryErrorClient.initialize"));
    }
}
