package de.fxnm.callable.connection.service;

import com.intellij.openapi.project.Project;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import de.fxnm.callable.BaseCallable;
import de.fxnm.web.grabber.CommonUrl;

public class ConnectionCallable extends BaseCallable<Boolean> {

    public ConnectionCallable(final Project project) {
        super(project);
    }

    @Override
    public Boolean call() {
        final boolean connectionStatus;
        super.startCallable("Checking Connection...", "Checking internet Connection");
        try {
            connectionStatus = this.checkConnectionStatus();
            super.finishedCallable("Internet Connection check finished");
            return connectionStatus;
        } catch (final IOException e) {
            super.failedCallable("Internet Connection check failed");
            return false;
        }
    }

    public boolean checkConnectionStatus() throws IOException {
        final URL url = new URL(CommonUrl.BASE.getUrl(this.project()));
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        return connection.getResponseCode() == HttpURLConnection.HTTP_OK;
    }
}
