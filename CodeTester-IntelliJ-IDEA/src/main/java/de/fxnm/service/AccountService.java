package de.fxnm.service;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;

import java.util.concurrent.Future;

import de.fxnm.listener.FeedbackListener;
import de.fxnm.listener.account.service.LogInFeedbackListener;
import de.fxnm.listener.account.service.LogOutFeedbackListener;
import de.fxnm.runnable.BaseRunnable;
import de.fxnm.runnable.account.service.LogInRunnable;
import de.fxnm.runnable.account.service.LogOutRunnable;
import de.fxnm.util.PooledThread;

public class AccountService extends BaseService {
    public AccountService(final Project project) {
        super(project);
    }

    public static AccountService getService(final Project project) {
        return ServiceManager.getService(project, AccountService.class);
    }


    public void asyncLogIn() {
        final LogInRunnable logInCallable = new LogInRunnable(this.project());
        logInCallable.addListener(new LogInFeedbackListener(this.project()));
        this.async(logInCallable);
    }

    public void asyncLogOut() {
        final LogOutRunnable logOutCallable = new LogOutRunnable(this.project());
        logOutCallable.addListener(new LogOutFeedbackListener(this.project()));
        this.async(logOutCallable);
    }

    private void async(final BaseRunnable runnable) {
        final Future<?> future = super.checkStart(PooledThread.execute(runnable), runnable);
        runnable.addListener(new ScanCompletionTracker(future));
    }


    private class ScanCompletionTracker extends FeedbackListener {

        private final Future<?> future;

        ScanCompletionTracker(final Future<?> future) {
            super(AccountService.super.project());
            this.future = future;
        }

        @Override
        public void scanStartingImp(final String toolWindowMessage, final String backGroundProcessName, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {

        }

        @Override
        public void scanCompletedImp(final String toolWindowMessage, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
            AccountService.this.checkComplete(this.future);
        }

        @Override
        public void scanFailedImp(final String toolWindowMessage, final Throwable throwable, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
            AccountService.this.checkComplete(this.future);
        }
    }
}
