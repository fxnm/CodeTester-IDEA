package de.fxnm.startup;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;

import org.jetbrains.annotations.NotNull;

import de.fxnm.errorhandling.SentryErrorClient;
import de.fxnm.service.ConnectionService;

public class StartUp implements StartupActivity {

    @Override
    public void runActivity(@NotNull final Project project) {
        if (ApplicationManager.getApplication().isUnitTestMode()) {
            return;
        }

        SentryErrorClient.init();

        ConnectionService.getService(project).asyncCheckConnection();
    }
}
