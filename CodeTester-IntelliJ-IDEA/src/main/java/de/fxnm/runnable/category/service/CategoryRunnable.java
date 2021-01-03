package de.fxnm.runnable.category.service;

import com.intellij.openapi.project.Project;

import java.io.IOException;

import de.fxnm.exceptions.InternetConnectionException;
import de.fxnm.exceptions.PasswordSafeException;
import de.fxnm.runnable.BaseRunnable;
import de.fxnm.util.CodeTesterBundle;
import de.fxnm.web.components.category.Category;
import de.fxnm.web.grabber.CheckCategoryGrabber;
import de.fxnm.web.grabber.access_token.AccessTokenGrabber;

public class CategoryRunnable extends BaseRunnable {

    public CategoryRunnable(final Project project) {
        super(project, CategoryRunnable.class);
    }

    @Override
    public void run() {
        super.startRunnable(CodeTesterBundle.message("plugin.runnable.category.start.loggerMessage"),
                CodeTesterBundle.message("plugin.runnable.category.start.toolWindowMessage"),
                CodeTesterBundle.message("plugin.runnable.category.start.backgroundProcessName"));
        try {
            final Category[] categories = CheckCategoryGrabber.getCategories(
                    this.project(),
                    AccessTokenGrabber.getToken(this.project()));

            super.finishedRunnable(CodeTesterBundle.message("plugin.runnable.category.finished.loggerMessage"),
                    CodeTesterBundle.message("plugin.runnable.category.finished.toolWindowMessage"),
                    categories);

        } catch (final Throwable e) {
            super.failedRunnable(CodeTesterBundle.message("plugin.runnable.category.failed.loggerMessage"),
                    CodeTesterBundle.message("plugin.runnable.category.failed.toolWindowMessage"),
                    e);
        }
    }
}
