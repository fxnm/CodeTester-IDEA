package de.fxnm.config.settings.global;


import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.fxnm.CodeTesterPlugin;
import lombok.Getter;

@State(
        name = CodeTesterPlugin.PLUGIN_ID + "-GlobalSettings",
        storages = {
                @Storage(
                        value = "CodeTesterGlobalSettings.xml"
                )
        }
)

public class GlobalSettingsService implements PersistentStateComponent<GlobalSettingsData> {

    @Getter
    private final GlobalSettingsData globalSettingsData = new GlobalSettingsData();

    public static GlobalSettingsService getService() {
        return ServiceManager.getService(GlobalSettingsService.class);
    }

    @Override
    public @Nullable GlobalSettingsData getState() {
        return this.globalSettingsData;
    }

    @Override
    public void loadState(@NotNull final GlobalSettingsData applicationSettings) {
        XmlSerializerUtil.copyBean(applicationSettings, this.globalSettingsData);
    }
}
