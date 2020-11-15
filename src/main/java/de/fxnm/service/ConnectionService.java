package de.fxnm.service;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;

import java.util.concurrent.Future;

import de.fxnm.callable.connection.service.ConnectionCallable;
import de.fxnm.listener.FeedbackListener;
import de.fxnm.listener.connection.service.ConnectionFeedbackListener;
import de.fxnm.util.PooledThread;

public class ConnectionService extends BaseService {

    public ConnectionService(final Project project) {
        super(project);
    }

    public static ConnectionService getService(final Project project) {
        return ServiceManager.getService(project, ConnectionService.class);
    }

    public void asyncCheckConnection() {
        final ConnectionCallable connectionCallable = new ConnectionCallable(this.project());
        connectionCallable.addListener(new ConnectionFeedbackListener(this.project()));
        this.async(connectionCallable);
    }

    private void async(final ConnectionCallable callable) {
        final Future<Boolean> future = super.checkStart(PooledThread.execute(callable));
        callable.addListener(new ConnectionService.ScanCompletionTracker(future));
    }

    private class ScanCompletionTracker extends FeedbackListener {

        private final Future<Boolean> future;

        ScanCompletionTracker(final Future<Boolean> future) {
            super(ConnectionService.super.project());
            this.future = future;
        }

        @Override
        public void scanStartingImp(final Object... details) {
        }

        @Override
        public void scanCompletedImp(final Object... details) {
            ConnectionService.this.checkComplete(this.future);
        }

        @Override
        public void scanFailedImp(final Object... details) {
            ConnectionService.this.checkComplete(this.future);
        }
    }
}
