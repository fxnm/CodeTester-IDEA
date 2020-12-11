package de.fxnm.runnable.category.service;

import com.intellij.openapi.project.Project;

import java.io.IOException;

import de.fxnm.exceptions.InternetConnectionException;
import de.fxnm.exceptions.PasswordSafeException;
import de.fxnm.runnable.BaseRunnable;
import de.fxnm.web.components.category.Category;
import de.fxnm.web.grabber.CheckCategoryGrabber;
import de.fxnm.web.grabber.access_token.AccessTokenGrabber;

public class CategoryRunnable extends BaseRunnable<CategoryRunnable> {

    public CategoryRunnable(final Project project) {
        super(project, CategoryRunnable.class);
    }

    @Override
    public void run() {
        super.startRunnable("Reloading...", "Started Reloading");
        try {
            final Category[] categories = CheckCategoryGrabber.getCategories(
                    this.project(),
                    AccessTokenGrabber.getToken(this.project()));

            super.finishedRunnable("Reload Successful", categories);

        } catch (final IOException | InternetConnectionException | PasswordSafeException e) {
            super.failedRunnable("Reload Failed");
            LOG.error("Reload Failed", e);
        }
    }
}
