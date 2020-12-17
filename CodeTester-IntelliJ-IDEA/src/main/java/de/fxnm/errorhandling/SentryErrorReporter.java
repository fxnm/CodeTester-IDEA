package de.fxnm.errorhandling;

import com.intellij.ide.DataManager;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.idea.IdeaLogger;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.ErrorReportSubmitter;
import com.intellij.openapi.diagnostic.IdeaLoggingEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.diagnostic.SubmittedReportInfo;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.util.Consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Component;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

import de.fxnm.util.CodeTesterBundle;
import de.fxnm.util.PopupNotifier;
import icons.PluginIcons;
import io.sentry.Sentry;
import io.sentry.SentryEvent;
import io.sentry.SentryLevel;
import io.sentry.protocol.Message;
import io.sentry.protocol.SentryId;


public class SentryErrorReporter extends ErrorReportSubmitter {

    private static final String DEFAULT_VALUE = "UNKNOWN";
    private static final Logger LOG = Logger.getInstance(SentryErrorReporter.class);

    @Override
    public String getPrivacyNoticeText() {
        return "<a href=\"https://github.com/fxnm/CodeTester-IDEA/wiki/Privacy-policy\">privacy statement</a>";
    }

    @NotNull
    @Override
    public String getReportActionText() {
        return CodeTesterBundle.message("plugin.error.sentryErrorReporter.reportActionText");
    }

    @Override
    public boolean submit(@NotNull final IdeaLoggingEvent @NotNull [] events,
                          @Nullable final String additionalInfo,
                          @NotNull final Component parentComponent,
                          @NotNull final Consumer<SubmittedReportInfo> consumer) {

        final DataContext context = DataManager.getInstance().getDataContext(parentComponent);
        final Project project = CommonDataKeys.PROJECT.getData(context);

        new Task.Backgroundable(project, CodeTesterBundle.message("plugin.error.sentryErrorReporter.taskName")) {
            @Override
            public void run(@NotNull final ProgressIndicator indicator) {
                final SentryEvent event = new SentryEvent();
                final Message message = new Message();

                event.setLevel(SentryLevel.ERROR);

                if (SentryErrorReporter.this.getPluginDescriptor() instanceof IdeaPluginDescriptor) {
                    event.setRelease(SentryErrorReporter.this.getPluginDescriptor().getVersion());
                }

                event.setThrowable(events[0].getThrowable());

                message.setMessage(String.format("User Message:%n%s%n%s",
                        additionalInfo,
                        Arrays.stream(events)
                                .map(IdeaLoggingEvent::getMessage)
                                .collect(Collectors.joining("\n"))));
                event.setMessage(message);

                final Properties properties = System.getProperties();
                event.setTag("JDK", properties.getProperty("java.runtime.version", DEFAULT_VALUE));
                event.setTag("VM", properties.getProperty("java.vm.name", DEFAULT_VALUE));
                event.setTag("OS", properties.getProperty("os.name", DEFAULT_VALUE));
                event.setTag("Architecture", properties.getProperty("os.arch", DEFAULT_VALUE));
                event.setTag("MachineId", properties.getProperty("io.netty.machineId", DEFAULT_VALUE));


                event.setTag("Last Action", IdeaLogger.ourLastActionId);
                event.setTag("Projekt Name", Objects.requireNonNull(project).getName());
                event.setTag("Projekt Location", project.getBasePath());


                @NotNull final SentryId captureEventId = Sentry.captureEvent(event);

                ApplicationManager.getApplication().invokeLater(() -> {
                    PopupNotifier.notify(project,
                            CodeTesterBundle.message("plugin.error.sentryErrorReporter.popup.title"),
                            CodeTesterBundle.message("plugin.error.sentryErrorReporter.popup.subtitle"),
                            String.format(CodeTesterBundle.message("plugin.error.sentryErrorReporter.popup.content"),
                                    captureEventId.toString()),
                            NotificationType.INFORMATION,
                            PluginIcons.STATUS_SUCCESS
                    );

                    consumer.consume(new SubmittedReportInfo(SubmittedReportInfo.SubmissionStatus.NEW_ISSUE));
                });
            }
        }.queue();

        LOG.info(CodeTesterBundle.message("plugin.error.sentryErrorReporter.successful"));

        return true;
    }
}
