package de.fxnm.config.settings.project.persistentstate;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;

import de.fxnm.CodeTesterPlugin;

@State(
        name = CodeTesterPlugin.PLUGIN_ID + "-ProjectPersistentSettings",
        storages = {
                @Storage(
                        value = "CodeTesterProjectSettings.xml"
                )
        }
)
public class ProjectPersistentSettingsService implements PersistentStateComponent<ProjectPersistentSettingsData> {

    @Getter
    private final ProjectPersistentSettingsData projectPersistentSettingsData = new ProjectPersistentSettingsData();

    public static ProjectPersistentSettingsService getService(final Project project) {
        return ServiceManager.getService(project, ProjectPersistentSettingsService.class);
    }

    @Override
    public @NotNull ProjectPersistentSettingsData getState() {
        return this.projectPersistentSettingsData;
    }

    @Override
    public void loadState(@NotNull final ProjectPersistentSettingsData state) {
        XmlSerializerUtil.copyBean(state, this.projectPersistentSettingsData);
    }
}
