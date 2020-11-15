package de.fxnm.config;

import de.fxnm.web.components.category.Category;
import de.fxnm.web.components.submission.SubmissionResult;

public interface ConfigurationListener {

    void displayErrorMessage(Boolean autoRemove, String message);

    void displayInfoMessage(Boolean autoRemove, String message);

    void displayCheckResult(SubmissionResult submissionResult);

    void removeCheckResult();

    void setCategories(Category[] categories);
}
