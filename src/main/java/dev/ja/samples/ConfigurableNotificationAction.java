package dev.ja.samples;

import com.intellij.notification.Notification;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

abstract class ConfigurableNotificationAction extends AnAction {
    ConfigurableNotificationAction(@Nls(capitalization = Nls.Capitalization.Title) @Nullable String text) {
        super(text);
    }

    @NotNull
    static ConfigurableNotificationAction create(@NotNull String text, @NotNull Consumer<NotificationConfigBuilder> performAction) {
        return new ConfigurableNotificationAction(text) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                ConfigurableNotification notification = (ConfigurableNotification) Notification.get(e);

                NotificationConfigBuilder b = NotificationConfigBuilder.create(e.getProject(), notification.config);
                b.resetActions();
                performAction.accept(b);
                // add default actions, based on the current settings
                b.addDefaultActions();

                // notification.expire();
                b.build().notify(e.getProject());
            }
        };
    }
}
