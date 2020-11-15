package de.fxnm.service;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;

import java.util.concurrent.Future;

import de.fxnm.callable.BaseCallable;
import de.fxnm.callable.account.service.LogInCallable;
import de.fxnm.callable.account.service.LogOutCallable;
import de.fxnm.listener.FeedbackListener;
import de.fxnm.listener.account.service.LogInFeedbackListener;
import de.fxnm.listener.account.service.LogOutFeedbackListener;
import de.fxnm.util.PooledThread;

public class AccountService extends BaseService {
    public AccountService(final Project project) {
        super(project);
    }

    public static AccountService getService(final Project project) {
        return ServiceManager.getService(project, AccountService.class);
    }


    public void asyncLogIn() {
        final BaseCallable<Boolean> logInCallable = new LogInCallable(this.project());
        logInCallable.addListener(new LogInFeedbackListener(this.project()));
        this.async(logInCallable);
    }

    public void asyncLogOut() {
        final BaseCallable<Boolean> logOutCallable = new LogOutCallable(this.project());
        logOutCallable.addListener(new LogOutFeedbackListener(this.project()));
        this.async(logOutCallable);
    }

    private void async(final BaseCallable<Boolean> callable) {
        final Future<Boolean> future = super.checkStart(PooledThread.execute(callable));
        callable.addListener(new ScanCompletionTracker(future));
    }


    private class ScanCompletionTracker extends FeedbackListener {

        private final Future<Boolean> future;

        ScanCompletionTracker(final Future<Boolean> future) {
            super(AccountService.super.project());
            this.future = future;
        }

        @Override
        public void scanStartingImp(final Object... details) {

        }

        @Override
        public void scanCompletedImp(final Object... details) {
            AccountService.this.checkComplete(this.future);
        }

        @Override
        public void scanFailedImp(final Object... details) {
            AccountService.this.checkComplete(this.future);
        }
    }
}
