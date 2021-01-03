package de.fxnm.service;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;

import java.util.concurrent.Future;

import de.fxnm.listener.FeedbackListener;
import de.fxnm.listener.category.service.ReloadFeedbackListener;
import de.fxnm.runnable.category.service.CategoryRunnable;
import de.fxnm.util.PooledThread;

public class CategoryService extends BaseService {

    public CategoryService(final Project project) {
        super(project);
    }

    public static CategoryService getService(final Project project) {
        return ServiceManager.getService(project, CategoryService.class);
    }

    public void asyncReloadCategories() {
        final CategoryRunnable logOutCallable = new CategoryRunnable(this.project());
        logOutCallable.addListener(new ReloadFeedbackListener(this.project()));
        this.async(logOutCallable);
    }

    private void async(final CategoryRunnable runnable) {
        final Future<?> future = super.checkStart(PooledThread.execute(runnable), runnable);
        runnable.addListener(new ScanCompletionTracker(future));
    }

    private class ScanCompletionTracker extends FeedbackListener {

        private final Future<?> future;

        ScanCompletionTracker(final Future<?> future) {
            super(CategoryService.super.project());
            this.future = future;
        }

        @Override
        public void scanStartingImp(final String toolWindowMessage, final String backGroundProcessName, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {

        }

        @Override
        public void scanCompletedImp(final String toolWindowMessage, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
            CategoryService.this.checkComplete(this.future);
        }

        @Override
        public void scanFailedImp(final String toolWindowMessage, final Throwable throwable, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
            CategoryService.this.checkComplete(this.future);
        }
    }
}
