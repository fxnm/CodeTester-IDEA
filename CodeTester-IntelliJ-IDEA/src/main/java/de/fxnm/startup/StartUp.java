package de.fxnm.startup;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;

import org.jetbrains.annotations.NotNull;

import de.fxnm.errorhandling.SentryErrorClient;
import de.fxnm.service.ConnectionService;
import de.fxnm.util.CodeTesterBundle;

public class StartUp implements StartupActivity {

    private static final Logger LOG = Logger.getInstance(StartUp.class);

    @Override
    public void runActivity(@NotNull final Project project) {
        if (ApplicationManager.getApplication().isUnitTestMode()) {
            return;
        }

        SentryErrorClient.initializeSentry();

        ConnectionService.getService(project).asyncCheckConnection();

        LOG.info(CodeTesterBundle.message("plugin.startup.successful"));
    }
}
