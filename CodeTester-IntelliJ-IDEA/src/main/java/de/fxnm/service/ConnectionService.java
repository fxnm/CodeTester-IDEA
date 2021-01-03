package de.fxnm.service;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;

import java.util.concurrent.Future;

import de.fxnm.listener.FeedbackListener;
import de.fxnm.listener.connection.service.ConnectionFeedbackListener;
import de.fxnm.runnable.connection.service.ConnectionRunnable;
import de.fxnm.util.PooledThread;

public class ConnectionService extends BaseService {

    public ConnectionService(final Project project) {
        super(project);
    }

    public static ConnectionService getService(final Project project) {
        return ServiceManager.getService(project, ConnectionService.class);
    }

    public void asyncCheckConnection() {
        final ConnectionRunnable connectionCallable = new ConnectionRunnable(this.project());
        connectionCallable.addListener(new ConnectionFeedbackListener(this.project()));
        this.async(connectionCallable);
    }

    private void async(final ConnectionRunnable runnable) {
        final Future<?> future = super.checkStart(PooledThread.execute(runnable), runnable);
        runnable.addListener(new ScanCompletionTracker(future));
    }

    private class ScanCompletionTracker extends FeedbackListener {

        private final Future<?> future;

        ScanCompletionTracker(final Future<?> future) {
            super(ConnectionService.super.project());
            this.future = future;
        }

        @Override
        public void scanStartingImp(final String toolWindowMessage, final String backGroundProcessName, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {

        }

        @Override
        public void scanCompletedImp(final String toolWindowMessage, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
            ConnectionService.this.checkComplete(this.future);
        }

        @Override
        public void scanFailedImp(final String toolWindowMessage, final Throwable throwable, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
            ConnectionService.this.checkComplete(this.future);

        }
    }
}
