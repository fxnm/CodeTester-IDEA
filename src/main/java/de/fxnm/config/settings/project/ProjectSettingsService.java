package de.fxnm.config.settings.project;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;

import org.jetbrains.annotations.NotNull;

import de.fxnm.CodeTesterPlugin;
import lombok.Getter;

@State(
        name = CodeTesterPlugin.PLUGIN_ID + "-ProjectSettings",
        storages = {
                @Storage(
                        value = "CodeTesterProjectSettings.xml"
                )
        }
)
public class ProjectSettingsService implements PersistentStateComponent<ProjectSettingsData> {

    @Getter
    private final ProjectSettingsData projectSettingsData = new ProjectSettingsData();

    public static ProjectSettingsService getService(final Project project) {
        return ServiceManager.getService(project, ProjectSettingsService.class);
    }

    @Override
    public @NotNull ProjectSettingsData getState() {
        return this.projectSettingsData;
    }

    @Override
    public void loadState(@NotNull final ProjectSettingsData state) {
        XmlSerializerUtil.copyBean(state, this.projectSettingsData);
    }
}
