package dev.ja.samples;

import com.google.common.collect.Lists;
import com.intellij.notification.Notification;
import com.intellij.notification.Notifications;
import com.intellij.notification.NotificationsAdapter;
import com.intellij.notification.NotificationsManager;
import com.intellij.openapi.project.Project;
import com.intellij.testFramework.LightPlatform4TestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Test;

import java.util.List;

public class ConfigurableNotificationTest extends LightPlatform4TestCase {
    /**
     * Expire notifications created in test methods.
     */
    @After
    public void expireAfterTest() {
        NotificationsManager mgr = NotificationsManager.getNotificationsManager();
        ConfigurableNotification[] notifications = mgr.getNotificationsOfType(ConfigurableNotification.class, getProject());
        for (ConfigurableNotification notification : notifications) {
            mgr.expire(notification);
        }
    }

    /**
     * This is one way to test notifications. Add a listener, store received notifications and then test what's been
     * stored.
     */
    @Test
    public void notificationsListener() {
        // register a listener to catch all notifications
        List<ConfigurableNotification> notifications = Lists.newCopyOnWriteArrayList();
        Project project = getProject();
        project.getMessageBus().connect(project).subscribe(Notifications.TOPIC, new NotificationsAdapter() {
            @Override
            public void notify(@NotNull Notification notification) {
                if (notification instanceof ConfigurableNotification) {
                    notifications.add((ConfigurableNotification) notification);
                }
            }
        });

        // create our notification
        NotificationConfigBuilder builder = NotificationConfigBuilder.create(project);
        builder.setContent("This is my content!");
        Notification notification = builder.build();

        // make sure that it has not been send yet
        assertEmpty(notifications);

        // send and test
        notification.notify(project);
        assertSize(1, notifications);
    }

    /**
     * This is another way to test notfications. Create and show notifications, then collect the unexpired notifications
     * and test what's there.
     */
    @Test
    public void notificationsCollector() {
        Project project = getProject();

        // create our notification
        NotificationConfigBuilder builder = NotificationConfigBuilder.create(project);
        builder.setContent("This is my content!");
        Notification notification = builder.build();

        // make sure it has not been send yet
        NotificationsManager mgr = NotificationsManager.getNotificationsManager();
        ConfigurableNotification[] notifications = mgr.getNotificationsOfType(ConfigurableNotification.class, project);
        assertEmpty(notifications);

        // send and test
        notification.notify(project);
        notifications = mgr.getNotificationsOfType(ConfigurableNotification.class, project);
        assertSize(1, notifications);
    }
}