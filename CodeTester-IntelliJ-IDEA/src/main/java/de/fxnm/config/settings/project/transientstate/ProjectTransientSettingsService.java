package de.fxnm.config.settings.project.transientstate;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;

public class ProjectTransientSettingsService {

    @Getter
    private final ProjectTransientSettingsData projectPersistentSettingsData = new ProjectTransientSettingsData();

    public static ProjectTransientSettingsService getService(final Project project) {
        return ServiceManager.getService(project, ProjectTransientSettingsService.class);
    }

    public @NotNull ProjectTransientSettingsData getState() {
        return this.projectPersistentSettingsData;
    }
}
