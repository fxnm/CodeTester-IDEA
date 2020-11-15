package de.fxnm.service;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;

import de.fxnm.toolwindow.ToolWindowAccess;
import de.fxnm.web.components.category.Category;
import lombok.Getter;
import lombok.Setter;

public class ProjectStateService {

    @Setter
    @Getter
    boolean manualLoginLogoutConfig = true;


    @Setter
    @Getter
    boolean manualRunConfig = true;

    @Setter
    @Getter
    boolean displayingError = true;

    @Setter
    @Getter
    boolean displayingSuccess = true;
    /**
     * Gibt an ob eine Server Verbindung zum Code Tester Server beseht.
     */
    @Getter
    private boolean serverConnectionEstablished = false;

    /**
     * Git an ob eine Login zu den Code Tester Servern besetzt.
     */
    @Getter
    private boolean loginConnectionEstablished = false;

    /**
     * Gibt an ob aktuell ein Test des Codes ausgeführt wird.
     */
    @Getter
    private boolean codeTesterRunning = false;

    public static ProjectStateService getService(final Project project) {
        return ServiceManager.getService(project, ProjectStateService.class);
    }

    public void setCodeTesterRunning(final boolean codeTesterRunning, final Project project) {
        this.codeTesterRunning = codeTesterRunning;
    }

    public void setLoginConnectionEstablished(final boolean loginConnectionEstablished, final Project project) {
        this.loginConnectionEstablished = loginConnectionEstablished;

        if (loginConnectionEstablished) {
            CategoryService.getService(project).asyncReloadCategories();
        } else {
            ToolWindowAccess.actOnToolWindowPanel(ToolWindowAccess.toolWindow(project),
                    codeTesterToolWindowPanel -> {
                        codeTesterToolWindowPanel.setCategories(new Category[0]);
                    }
            );
        }
    }

    public void setServerConnectionEstablished(final boolean serverConnectionEstablished, final Project project) {
        this.serverConnectionEstablished = serverConnectionEstablished;
    }


}
