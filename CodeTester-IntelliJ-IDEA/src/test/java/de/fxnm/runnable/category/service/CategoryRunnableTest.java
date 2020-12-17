package de.fxnm.runnable.category.service;

import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;

import org.junit.jupiter.api.Assertions;

import de.fxnm.config.settings.password_safe.PasswordManager;
import de.fxnm.listener.Listener;
import de.fxnm.util.CodeTesterBundle;
import de.fxnm.util.EnvironmentVariable;
import de.fxnm.web.components.category.Category;

import static testing.Util.checkEquality;
import static testing.Util.setEmptyCredentials;

public class CategoryRunnableTest extends LightJavaCodeInsightFixtureTestCase {

    private CategoryRunnable categoryRunnable;

    public void testGetCategoriesNoLoggingData() {
        this.categoryRunnable = new CategoryRunnable(this.getProject());
        this.categoryRunnable.addListener(new Listener(this.getProject()) {
            @Override
            public void scanStartingImp(final String toolWindowMessage, final String backGroundProcessName, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                checkEquality(
                        CodeTesterBundle.message("plugin.runnable.category.start.toolWindowMessage"), toolWindowMessage,
                        CodeTesterBundle.message("plugin.runnable.category.start.backgroundProcessName"), backGroundProcessName,
                        null, argumentOne,
                        null, argumentTwo,
                        null, argumentThree
                );
            }

            @Override
            public void scanCompletedImp(final String toolWindowMessage, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                Assertions.fail();
            }

            @Override
            public void scanFailedImp(final String toolWindowMessage, final Throwable throwable, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                checkEquality(
                        CodeTesterBundle.message("plugin.runnable.category.failed.toolWindowMessage"), toolWindowMessage,
                        null, argumentOne,
                        null, argumentTwo,
                        null, argumentThree
                );
            }
        });

        this.categoryRunnable.run();
    }

    public void testGetCategories() {
        PasswordManager.store(PasswordManager.LOGIN_KEY, EnvironmentVariable.get(EnvironmentVariable.USERNAME), EnvironmentVariable.get(EnvironmentVariable.PASSWORD));


        this.categoryRunnable = new CategoryRunnable(this.getProject());
        this.categoryRunnable.addListener(new Listener(this.getProject()) {
            @Override
            public void scanStartingImp(final String toolWindowMessage, final String backGroundProcessName, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                checkEquality(
                        CodeTesterBundle.message("plugin.runnable.category.start.toolWindowMessage"), toolWindowMessage,
                        CodeTesterBundle.message("plugin.runnable.category.start.backgroundProcessName"), backGroundProcessName,
                        null, argumentOne,
                        null, argumentTwo,
                        null, argumentThree
                );
            }

            @Override
            public void scanCompletedImp(final String toolWindowMessage, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                checkEquality(
                        CodeTesterBundle.message("plugin.runnable.category.finished.toolWindowMessage"), toolWindowMessage,
                        null, argumentTwo,
                        null, argumentThree
                );

                Assertions.assertNotNull(argumentOne);
                final Category[] categories = (Category[]) argumentOne;

                Assertions.assertNotEquals(0, categories.length);
            }

            @Override
            public void scanFailedImp(final String toolWindowMessage, final Throwable throwable, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                fail();

            }
        });

        this.categoryRunnable.run();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setEmptyCredentials();
    }
}