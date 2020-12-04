package de.fxnm.web.grabber;

import com.intellij.openapi.project.Project;

import de.fxnm.config.settings.project.ProjectSettingsService;

public enum CommonUrl {
    BASE("/"),
    LOGIN("/login"),
    ACCESS_TOKEN("/login/get-access-token"),
    CHECK_CATEGORY("/check-category/get-all"),
    RUN_CHECK("/test/multiple/"),
    CREATE_NEW_TEST("/#/submit-check");

    private final String url;

    CommonUrl(final String url) {
        this.url = url;
    }

    public String getUrl(final Project project) {
        return ProjectSettingsService.getService(project).getState().getSelectedCodeTesterBaseURL() + this.url;
    }
}
