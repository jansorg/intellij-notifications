package dev.ja.samples;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class DemoNotificationService {
    public static DemoNotificationService getInstance() {
        return ServiceManager.getService(DemoNotificationService.class);
    }

    public void showWithDefaultSettings(@NotNull Project project) {
        NotificationConfigBuilder builder = NotificationConfigBuilder.create(project);
        builder.setNotificationType(NotificationType.INFORMATION);
        builder.setTitle("Default notification");
        builder.setSubtitle("A configurable notification");
        builder.setContent("This notification displays all possible settings.<br>" +
                "Use the <strong>actions</strong> to <em>alter settings</em> and to redisplay a notification with the <u>new settings</u>.");
        builder.addDefaultActions();
        builder.build().notify(project);
    }
}
