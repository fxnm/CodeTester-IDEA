package de.fxnm.service;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;

import java.util.concurrent.Future;

import de.fxnm.listener.FeedbackListener;
import de.fxnm.listener.scanner.service.UiScannerFeedbackListener;
import de.fxnm.runnable.scanner.service.CheckFilesRunnable;
import de.fxnm.util.PooledThread;

public class ScannerService extends BaseService {

    public ScannerService(final Project project) {
        super(project);
    }

    public static ScannerService getService(final Project project) {
        return ServiceManager.getService(project, ScannerService.class);
    }

    public void asyncScanFiles(final int checkId) {

        ApplicationManager.getApplication().saveAll();

        final CheckFilesRunnable scanFilesCallable = new CheckFilesRunnable(this.project(), checkId);
        scanFilesCallable.addListener(new UiScannerFeedbackListener(this.project()));
        this.runAsyncCodeTester(scanFilesCallable);
    }

    private void runAsyncCodeTester(final CheckFilesRunnable runnable) {
        final Future<?> checkFilesFuture = super.checkStart(PooledThread.execute(runnable), runnable);
        runnable.addListener(new ScanCompletionTracker(checkFilesFuture));
    }


    private class ScanCompletionTracker extends FeedbackListener {

        private final Future<?> future;

        ScanCompletionTracker(final Future<?> future) {
            super(ScannerService.super.project());
            this.future = future;
        }

        @Override
        public void scanStartingImp(final String toolWindowMessage,
                                    final String backGroundProcessName,
                                    final Object argumentOne,
                                    final Object argumentTwo,
                                    final Object argumentThree) {
        }

        @Override
        public void scanCompletedImp(final String toolWindowMessage,
                                     final Object argumentOne,
                                     final Object argumentTwo,
                                     final Object argumentThree) {
            ScannerService.this.checkComplete(this.future);
        }

        @Override
        public void scanFailedImp(final String toolWindowMessage,
                                  final Throwable throwable,
                                  final Object argumentOne,
                                  final Object argumentTwo,
                                  final Object argumentThree) {
            ScannerService.this.checkComplete(this.future);
        }
    }
}
