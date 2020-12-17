package de.fxnm.runnable.connection.service;

import com.intellij.openapi.project.Project;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import de.fxnm.runnable.BaseRunnable;
import de.fxnm.util.CodeTesterBundle;
import de.fxnm.web.grabber.CommonUrl;

public class ConnectionRunnable extends BaseRunnable {

    public ConnectionRunnable(final Project project) {
        super(project, ConnectionRunnable.class);
    }

    @Override
    public void run() {
        super.startRunnable(CodeTesterBundle.message("plugin.runnable.connection.start.loggerMessage"),
                CodeTesterBundle.message("plugin.runnable.connection.start.toolWindowMessage"),
                CodeTesterBundle.message("plugin.runnable.connection.start.backgroundProcessName"));
        try {
            if (!this.checkConnectionStatus()) {
                super.failedRunnable(CodeTesterBundle.message("plugin.runnable.connection.failed.noConnection.loggerMessage"),
                        CodeTesterBundle.message("plugin.runnable.connection.failed.noConnection.toolWindowMessage"),
                        null,
                        CodeTesterBundle.message("plugin.runnable.connection.failed.noConnection.notificationMessage"));
                return;
            }

            super.finishedRunnable(CodeTesterBundle.message("plugin.runnable.connection.finished.loggerMessage"),
                    CodeTesterBundle.message("plugin.runnable.connection.finished.toolWindowMessage"));
        } catch (final Throwable e) {
            super.failedRunnable(CodeTesterBundle.message("plugin.runnable.connection.failed.throwable.loggerMessage"),
                    CodeTesterBundle.message("plugin.runnable.connection.failed.throwable.toolWindowMessage"),
                    e,
                    CodeTesterBundle.message("plugin.runnable.connection.failed.throwable.notificationMessage"));
        }
    }

    public boolean checkConnectionStatus() throws IOException {
        final URL url = new URL(CommonUrl.BASE.getUrl(this.project()));
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.disconnect();
        return connection.getResponseCode() == HttpURLConnection.HTTP_OK;
    }
}
