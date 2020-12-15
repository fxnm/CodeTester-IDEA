package de.fxnm.runnable.connection.service;

import com.intellij.openapi.project.Project;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import de.fxnm.runnable.BaseRunnable;
import de.fxnm.web.grabber.CommonUrl;

public class ConnectionRunnable extends BaseRunnable {

    public ConnectionRunnable(final Project project) {
        super(project, ConnectionRunnable.class);
    }

    @Override
    public void run() {
        super.startRunnable("Starting CodeTester Server Connection Check Runnable",
                "Checking Server connection",
                "Checking Server Connection...");
        try {
            if (!this.checkConnectionStatus()) {
                super.failedRunnable("Server Connection Check Failed", "Server Connection Failed, try it again later");
                return;
            }

            super.finishedRunnable("Server Connection Check successful and Runnable finished", "Server Connection established");
        } catch (final IOException e) {
            super.failedRunnable("Server Connection Check Runnable Failed",
                    "Server connection failed, try it again later", e);
        }
    }

    public boolean checkConnectionStatus() throws IOException {
        final URL url = new URL(CommonUrl.BASE.getUrl(this.project()));
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        return connection.getResponseCode() == HttpURLConnection.HTTP_OK;
    }
}
