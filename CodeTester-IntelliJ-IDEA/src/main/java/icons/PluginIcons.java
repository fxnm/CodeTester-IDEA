package icons;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;

import javax.swing.Icon;

public interface PluginIcons {
    Icon PLUGIN_ICON = AllIcons.Modules.TestResourcesRoot;

    Icon LOGIN_GREEN = IconLoader.getIcon("/de/fxnm/icons/login/status/LoginGreen.svg", PluginIcons.class);
    Icon LOGIN_RED = IconLoader.getIcon("/de/fxnm/icons/login/status/LoginRed.svg", PluginIcons.class);
    Icon REMOVE = IconLoader.getIcon("/de/fxnm/icons/util/Remove.svg", PluginIcons.class);


    Icon STATUS_SUCCESS = AllIcons.Actions.Commit;
    Icon STATUS_ERROR = AllIcons.Vcs.Remove;
    Icon STATUS_INFO = AllIcons.General.BalloonInformation;

    Icon SHOW_ERROR = AllIcons.General.BalloonWarning;
    Icon SHOW_SUCCESS = AllIcons.General.InspectionsOK;

    Icon COPY = AllIcons.General.CopyHovered;
}
