package de.fxnm.runnable.connection.service;

import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;

import org.jetbrains.annotations.NotNull;

import de.fxnm.config.settings.project.persistentstate.ProjectPersistentSettingsData;
import de.fxnm.config.settings.project.persistentstate.ProjectPersistentSettingsService;
import de.fxnm.listener.Listener;
import de.fxnm.util.CodeTesterBundle;

import static testing.Util.checkEquality;

public class ConnectionRunnableTest extends LightJavaCodeInsightFixtureTestCase {

    private ConnectionRunnable connectionRunnable;

    public void testConnection() {
        this.connectionRunnable = new ConnectionRunnable(this.getProject());
        this.connectionRunnable.addListener(new Listener(this.getProject()) {
            @Override
            public void scanStartingImp(final String toolWindowMessage, final String backGroundProcessName, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                checkEquality(
                        CodeTesterBundle.message("plugin.runnable.connection.start.toolWindowMessage"), toolWindowMessage,
                        CodeTesterBundle.message("plugin.runnable.connection.start.backgroundProcessName"), backGroundProcessName,
                        null, argumentOne,
                        null, argumentTwo,
                        null, argumentThree
                );
            }

            @Override
            public void scanCompletedImp(final String toolWindowMessage, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                checkEquality(
                        CodeTesterBundle.message("plugin.runnable.connection.finished.toolWindowMessage"), toolWindowMessage,
                        null, argumentOne,
                        null, argumentTwo,
                        null, argumentThree
                );
            }

            @Override
            public void scanFailedImp(final String toolWindowMessage, final Throwable throwable, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                fail();
            }
        });

        this.connectionRunnable.run();
    }

    public void testConnectionWrongURL() {
        ProjectPersistentSettingsService.getService(this.getProject()).getState().setSelectedCodeTesterBaseURL("");

        this.connectionRunnable = new ConnectionRunnable(this.getProject());
        this.connectionRunnable.addListener(new Listener(this.getProject()) {
            @Override
            public void scanStartingImp(final String toolWindowMessage, final String backGroundProcessName, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                checkEquality(
                        CodeTesterBundle.message("plugin.runnable.connection.start.toolWindowMessage"), toolWindowMessage,
                        CodeTesterBundle.message("plugin.runnable.connection.start.backgroundProcessName"), backGroundProcessName,
                        null, argumentOne,
                        null, argumentTwo,
                        null, argumentThree
                );
            }

            @Override
            public void scanCompletedImp(final String toolWindowMessage, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
               fail();
            }

            @Override
            public void scanFailedImp(final String toolWindowMessage, final Throwable throwable, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                checkEquality(
                        CodeTesterBundle.message("plugin.runnable.connection.failed.throwable.toolWindowMessage"), toolWindowMessage,
                        CodeTesterBundle.message("plugin.runnable.connection.failed.throwable.notificationMessage"), argumentOne,
                        null, argumentTwo,
                        null, argumentThree
                );
            }
        });

        this.connectionRunnable.run();
    }
}