package de.fxnm.service;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;

import de.fxnm.toolwindow.ToolWindowAccess;
import de.fxnm.web.components.category.Category;
import lombok.Getter;
import lombok.Setter;

// TODO: 04.12.2020 REMOVE THIS!
public class ProjectStateService {

    @Setter
    @Getter
    boolean displayingError = true;
    @Setter
    @Getter
    boolean displayingSuccess = true;
    @Setter
    @Getter
    boolean manualLoginLogoutConfig = true;
    @Setter
    @Getter
    boolean manualRunConfig = true;
    /**
     * Git an ob eine Login zu den Code Tester Servern besetzt.
     */
    @Getter
    private boolean loginConnectionEstablished = false;
    /**
     * Gibt an ob eine Server Verbindung zum Code Tester Server beseht.
     */
    @Getter
    private boolean serverConnectionEstablished = false;

    public static ProjectStateService getService(final Project project) {
        return ServiceManager.getService(project, ProjectStateService.class);
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
