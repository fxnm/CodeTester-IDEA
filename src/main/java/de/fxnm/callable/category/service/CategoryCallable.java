package de.fxnm.callable.category.service;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;

import java.io.IOException;

import de.fxnm.callable.BaseCallable;
import de.fxnm.exceptions.InternetConnectionException;
import de.fxnm.exceptions.PasswordSafeException;
import de.fxnm.web.components.category.Category;
import de.fxnm.web.grabber.CheckCategoryGrabber;
import de.fxnm.web.grabber.access_token.AccessTokenGrabber;

public class CategoryCallable extends BaseCallable<Category[]> {

    private static final Logger LOG = Logger.getInstance(CategoryCallable.class);

    public CategoryCallable(final Project project) {
        super(project);
    }

    @Override
    public Category[] call() {
        Category[] categories = new Category[]{};
        super.startCallable("Reloading...", "Started Reloading");
        try {
            categories = CheckCategoryGrabber.getCategories(this.project(),
                    AccessTokenGrabber.getToken(this.project()));

            super.finishedCallable("Reload Successful", categories);

        } catch (final IOException | InternetConnectionException | PasswordSafeException e) {
            super.failedCallable("Reload Failed");
            LOG.error("Reload Failed", e);
            return categories;
        }
        return categories;
    }
}
