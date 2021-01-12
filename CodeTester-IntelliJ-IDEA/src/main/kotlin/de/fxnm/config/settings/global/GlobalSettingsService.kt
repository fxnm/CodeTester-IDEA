package de.fxnm.config.settings.global

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import de.fxnm.CodeTesterPlugin
import de.fxnm.config.settings.global.GlobalSettingsService
import lombok.Getter

@State(name = CodeTesterPlugin.PLUGIN_ID + "-GlobalSettings",
       storages = [Storage(value = "CodeTesterGlobalSettings.xml")])

class GlobalSettingsService : PersistentStateComponent<GlobalSettingsData?> {

    @Getter
    private val globalSettingsData = GlobalSettingsData()
    override fun getState(): GlobalSettingsData {
        return globalSettingsData
    }

    override fun loadState(applicationSettings: GlobalSettingsData) {
        XmlSerializerUtil.copyBean(applicationSettings, globalSettingsData)
    }

    companion object {
        @JvmStatic
        val service: GlobalSettingsService
            get() = ServiceManager.getService(GlobalSettingsService::class.java)
    }
}
