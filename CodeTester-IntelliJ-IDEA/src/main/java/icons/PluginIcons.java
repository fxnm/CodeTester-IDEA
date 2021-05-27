package icons;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;

import javax.swing.Icon;

public interface PluginIcons {
    Icon COPY = AllIcons.General.CopyHovered;
    Icon LOGIN_GREEN = IconLoader.getIcon("/de/fxnm/icons/login/status/LoginGreen.svg", PluginIcons.class);
    Icon LOGIN_RED = IconLoader.getIcon("/de/fxnm/icons/login/status/LoginRed.svg", PluginIcons.class);
    Icon PLUGIN_ICON = IconLoader.getIcon("/META-INF/pluginIcon.svg", PluginIcons.class);
    Icon REMOVE = IconLoader.getIcon("/de/fxnm/icons/util/Remove.svg", PluginIcons.class);
    Icon SHOW_ERROR = AllIcons.General.BalloonWarning;
    Icon SHOW_SUCCESS = AllIcons.General.InspectionsOK;
    Icon STATUS_ERROR = AllIcons.Vcs.Remove;
    Icon STATUS_INFO = AllIcons.General.BalloonInformation;
    Icon STATUS_SUCCESS = AllIcons.Actions.Commit;
    Icon UNKNOWN = AllIcons.Nodes.Unknown;
    Icon WARNING = AllIcons.General.Warning;
}
