package dev.ja.samples;

import com.intellij.notification.Notification;
import com.intellij.openapi.actionSystem.AnAction;
import org.jetbrains.annotations.NotNull;

class ConfigurableNotification extends Notification {
    @NotNull
    final NotificationConfig config;

    public ConfigurableNotification(@NotNull NotificationConfig config) {
        super(config.group.getDisplayId(), null, config.notificationType);
        this.config = config;

        setTitle(config.title, config.subtitle);
        setContent(config.content);
        setIcon(config.icon);

        setImportant(config.isImportant);

        if (config.actions != null) {
            for (AnAction action : config.actions) {
                addAction(action);
            }
        }

        if (config.dropdownText != null) {
            setDropDownText(config.dropdownText);
        }

        if (config.collapseDirection != null) {
            setCollapseActionsDirection(config.collapseDirection);
        }
    }
}
