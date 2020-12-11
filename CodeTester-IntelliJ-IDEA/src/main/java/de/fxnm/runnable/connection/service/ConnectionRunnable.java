package de.fxnm.runnable.connection.service;

import com.intellij.openapi.project.Project;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import de.fxnm.runnable.BaseRunnable;
import de.fxnm.web.grabber.CommonUrl;

public class ConnectionRunnable extends BaseRunnable<ConnectionRunnable> {

    public ConnectionRunnable(final Project project) {
        super(project, ConnectionRunnable.class);
    }

    @Override
    public void run() {
        super.startRunnable("Checking Connection...", "Checking internet Connection");
        try {
            if (this.checkConnectionStatus()) {
                super.finishedRunnable("Internet Connection check finished");
            } else {
                super.failedRunnable("Internet Connection check failed");
            }
        } catch (final IOException e) {
            super.failedRunnable("Internet Connection check failed");
        }
    }

    public boolean checkConnectionStatus() throws IOException {
        final URL url = new URL(CommonUrl.BASE.getUrl(this.project()));
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        return connection.getResponseCode() == HttpURLConnection.HTTP_OK;
    }
}
