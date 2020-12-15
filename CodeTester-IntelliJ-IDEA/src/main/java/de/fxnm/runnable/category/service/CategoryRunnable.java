package de.fxnm.runnable.category.service;

import com.intellij.openapi.project.Project;

import java.io.IOException;

import de.fxnm.exceptions.InternetConnectionException;
import de.fxnm.exceptions.PasswordSafeException;
import de.fxnm.runnable.BaseRunnable;
import de.fxnm.web.components.category.Category;
import de.fxnm.web.grabber.CheckCategoryGrabber;
import de.fxnm.web.grabber.access_token.AccessTokenGrabber;

public class CategoryRunnable extends BaseRunnable {

    public CategoryRunnable(final Project project) {
        super(project, CategoryRunnable.class);
    }

    @Override
    public void run() {
        super.startRunnable("Starting Category Reload Runnable", "Started Reloading", "Reload...");
        try {
            final Category[] categories = CheckCategoryGrabber.getCategories(
                    this.project(),
                    AccessTokenGrabber.getToken(this.project()));

            super.finishedRunnable("Category Reload was successful and Runnable finished with new Category set",
                    "Reload Successful", categories);

        } catch (final IOException | InternetConnectionException | PasswordSafeException e) {
            super.failedRunnable("Category Reload Runnable Failed", "Failed to Reload, try it again later", e);
        }
    }
}
