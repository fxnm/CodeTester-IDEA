package testing;

import com.google.gson.Gson;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;

import org.jetbrains.annotations.TestOnly;

import de.fxnm.config.settings.global.GlobalSettingsData;
import de.fxnm.config.settings.global.GlobalSettingsService;
import de.fxnm.config.settings.password_safe.PasswordManager;
import de.fxnm.config.settings.project.persistentstate.ProjectPersistentSettingsData;
import de.fxnm.config.settings.project.persistentstate.ProjectPersistentSettingsService;
import de.fxnm.config.settings.project.transientstate.ProjectTransientSettingsData;
import de.fxnm.config.settings.project.transientstate.ProjectTransientSettingsService;

@TestOnly
public abstract class TestingBase extends LightJavaCodeInsightFixtureTestCase {

    private static final String NOTHING = " ";

    private GlobalSettingsData deepCopy_globalSettingsData;
    private ProjectPersistentSettingsData deepCopy_persistentSettingsData;
    private ProjectTransientSettingsData transientSettingsData;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        PasswordManager.store(PasswordManager.LOGIN_KEY, NOTHING, NOTHING);
        PasswordManager.store(PasswordManager.TEST_LOGIN_KEY, NOTHING, NOTHING);

        final GlobalSettingsData globalSettingsData = GlobalSettingsService.getService().getState();
        final ProjectPersistentSettingsData persistentSettingsData = ProjectPersistentSettingsService.getService(this.getProject()).getState();
        final ProjectTransientSettingsData projectTransientSettingsData = ProjectTransientSettingsService.getService(this.getProject()).getState();

        final Gson gson = new Gson();

        this.deepCopy_globalSettingsData = gson.fromJson(gson.toJson(globalSettingsData), GlobalSettingsData.class);
        this.deepCopy_persistentSettingsData = gson.fromJson(gson.toJson(persistentSettingsData), ProjectPersistentSettingsData.class);
        this.transientSettingsData = gson.fromJson(gson.toJson(projectTransientSettingsData), ProjectTransientSettingsData.class);
    }


    @Override
    protected void tearDown() throws Exception {
        GlobalSettingsService.getService().loadState(this.deepCopy_globalSettingsData);
        ProjectPersistentSettingsService.getService(this.getProject()).loadState(this.deepCopy_persistentSettingsData);
        ProjectTransientSettingsService.getService(this.getProject()).loadState(this.transientSettingsData);

        super.tearDown();
    }
}
