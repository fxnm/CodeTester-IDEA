package de.fxnm.service;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;

import java.util.concurrent.Future;

import de.fxnm.callable.category.service.CategoryCallable;
import de.fxnm.listener.FeedbackListener;
import de.fxnm.listener.category.service.ReloadFeedbackListener;
import de.fxnm.util.PooledThread;
import de.fxnm.web.components.category.Category;

public class CategoryService extends BaseService {

    public CategoryService(final Project project) {
        super(project);
    }

    public static CategoryService getService(final Project project) {
        return ServiceManager.getService(project, CategoryService.class);
    }

    public void asyncReloadCategories() {
        final CategoryCallable logOutCallable = new CategoryCallable(this.project());
        logOutCallable.addListener(new ReloadFeedbackListener(this.project()));
        this.async(logOutCallable);
    }

    private void async(final CategoryCallable callable) {
        final Future<Category[]> future = super.checkStart(PooledThread.execute(callable));
        callable.addListener(new CategoryService.ScanCompletionTracker(future));
    }

    private class ScanCompletionTracker extends FeedbackListener {

        private final Future<Category[]> future;

        ScanCompletionTracker(final Future<Category[]> future) {
            super(CategoryService.super.project());
            this.future = future;
        }

        @Override
        public void scanStartingImp(final Object... details) {
        }

        @Override
        public void scanCompletedImp(final Object... details) {
            CategoryService.this.checkComplete(this.future);
        }

        @Override
        public void scanFailedImp(final Object... details) {
            CategoryService.this.checkComplete(this.future);
        }
    }
}
