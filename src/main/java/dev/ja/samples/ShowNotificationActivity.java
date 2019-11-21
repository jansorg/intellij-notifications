package dev.ja.samples;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;

/**
 * Startup activity, which displays the initial notification.
 */
public class ShowNotificationActivity implements StartupActivity {
    @Override
    public void runActivity(@NotNull Project project) {
        NotificationConfigBuilder builder = NotificationConfigBuilder.create(project);
        builder.setNotificationType(NotificationType.INFORMATION);
        builder.setTitle("Configurable Notification");
        builder.setSubtitle("user-defined properties");
        builder.setContent("<html>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt " +
                "ut labore et dolore magna aliqua. Ut enim ad minim veniam, " +
                "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur." +
                "<br><strong style=\"font-size:larger;\">Open article on <a href=\"https://www.plugin-dev.com/\">plugin-dev.com</a>.</strong></html>");
        builder.addDefaultActions();
        builder.build().notify(project);
    }
}
