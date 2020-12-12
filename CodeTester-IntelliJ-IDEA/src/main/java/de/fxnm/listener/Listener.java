package de.fxnm.listener;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;

public abstract class Listener {

    private final Project project;

    public Listener(final Project project) {
        this.project = project;
    }

    public void scanStarting(final String toolWindowMessage,
                             final String backGroundProcessName,
                             final Object argumentOne,
                             final Object argumentTwo,
                             final Object argumentThree) {

        ApplicationManager.getApplication().invokeLater(() -> this.scanStartingImp(toolWindowMessage, backGroundProcessName, argumentOne, argumentTwo, argumentThree));
    }

    public abstract void scanStartingImp(String toolWindowMessage,
                                         String backGroundProcessName,
                                         Object argumentOne,
                                         Object argumentTwo,
                                         Object argumentThree);


    public void scanCompleted(final String toolWindowMessage,
                              final Object argumentOne,
                              final Object argumentTwo,
                              final Object argumentThree) {

        ApplicationManager.getApplication().invokeLater(() -> this.scanCompletedImp(toolWindowMessage, argumentOne, argumentTwo, argumentThree));
    }

    public abstract void scanCompletedImp(String toolWindowMessage,
                                          Object argumentOne,
                                          Object argumentTwo,
                                          Object argumentThree);


    public void scanFailed(final String toolWindowMessage,
                           final Throwable throwable,
                           final Object argumentOne,
                           final Object argumentTwo,
                           final Object argumentThree) {

        ApplicationManager.getApplication().invokeLater(() -> this.scanFailedImp(toolWindowMessage, throwable, argumentOne, argumentTwo, argumentThree));
    }

    public abstract void scanFailedImp(final String toolWindowMessage,
                                       final Throwable throwable,
                                       final Object argumentOne,
                                       final Object argumentTwo,
                                       final Object argumentThree);

    public Project project() {
        return this.project;
    }
}
