package de.fxnm.service;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileVisitor;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import de.fxnm.callable.scanner.service.ScanFilesCallable;
import de.fxnm.listener.FeedbackListener;
import de.fxnm.listener.scanner.service.UiScannerFeedbackListener;
import de.fxnm.util.PooledThread;
import de.fxnm.web.components.submission.SubmissionResult;

public class ScannerService extends BaseService {

    private static final Logger LOG = Logger.getInstance(ScannerService.class);

    public ScannerService(final Project project) {
        super(project);
    }

    public static ScannerService getService(final Project project) {
        return ServiceManager.getService(project, ScannerService.class);
    }

    public void asyncScanFiles(final int checkId) {
        final List<VirtualFile> files = this.getChildrenFiles(this.getProjectRootFiles(this.project()));

        if (files.isEmpty()) {
            LOG.error("No file provided");
            return;
        }

        final ScanFilesCallable scanFilesCallable = new ScanFilesCallable(this.project(), files, checkId);
        scanFilesCallable.addListener(new UiScannerFeedbackListener(this.project()));
        this.runAsyncCodeTester(scanFilesCallable);
    }

    private void runAsyncCodeTester(final ScanFilesCallable scanFilesCallable) {
        final Future<SubmissionResult> checkFilesFuture = super.checkStart(PooledThread.execute(scanFilesCallable));
        scanFilesCallable.addListener(new ScanCompletionTracker(checkFilesFuture));
    }


    private @NotNull VirtualFile[] getProjectRootFiles(final Project project) {
        return ProjectRootManager.getInstance(project).getContentRoots();

    }

    private List<VirtualFile> getChildrenFiles(final VirtualFile[] files) {
        final List<VirtualFile> flattened = new ArrayList<>();
        if (files != null) {
            for (final VirtualFile file : files) {
                flattened.add(file);
                VfsUtilCore.visitChildrenRecursively(file, new VirtualFileVisitor<Object>() {
                    @Override
                    @NotNull
                    public Result visitFileEx(@NotNull final VirtualFile file) {
                        flattened.add(file);
                        return CONTINUE;
                    }
                });
            }
        }
        return flattened;
    }

    private class ScanCompletionTracker extends FeedbackListener {

        private final Future<SubmissionResult> future;

        ScanCompletionTracker(final Future<SubmissionResult> future) {
            super(ScannerService.super.project());
            this.future = future;
        }

        @Override
        public void scanStartingImp(final Object... details) {

        }

        @Override
        public void scanCompletedImp(final Object... details) {
            ScannerService.this.checkComplete(this.future);
        }

        @Override
        public void scanFailedImp(final Object... details) {
            ScannerService.this.checkComplete(this.future);
        }
    }
}
